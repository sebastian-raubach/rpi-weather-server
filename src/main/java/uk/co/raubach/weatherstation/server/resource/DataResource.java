package uk.co.raubach.weatherstation.server.resource;

import org.jooq.*;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.data.Status;
import org.restlet.data.*;
import org.restlet.representation.FileRepresentation;
import org.restlet.resource.*;
import uk.co.raubach.weatherstation.resource.MeasurementPojo;
import uk.co.raubach.weatherstation.server.database.Database;
import uk.co.raubach.weatherstation.server.database.codegen.tables.pojos.Measurements;
import uk.co.raubach.weatherstation.server.database.codegen.tables.records.MeasurementsRecord;
import uk.co.raubach.weatherstation.server.util.PropertyWatcher;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.sql.*;
import java.time.*;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

import static uk.co.raubach.weatherstation.server.database.codegen.tables.Measurements.*;

public class DataResource extends ServerResource
{
	public static final String PARAM_UUID  = "uuid";
	public static final String PARAM_START = "start";
	public static final String PARAM_END   = "end";

	private Timestamp start;
	private Timestamp end;

	private String uuid;

	private BigDecimal windOffset;
	private BigDecimal windFactor;
	private BigDecimal tempOffset;

	@Override
	protected void doInit()
		throws ResourceException
	{
		super.doInit();

		try
		{
			this.windOffset = BigDecimal.valueOf(Double.parseDouble(PropertyWatcher.get("wind.direction.offset")));
		}
		catch (Exception e)
		{
			this.windOffset = BigDecimal.valueOf(0.0d);
		}

		try
		{
			this.windFactor = BigDecimal.valueOf(Double.parseDouble(PropertyWatcher.get("wind.strength.multiplier")));
		}
		catch (Exception e)
		{
			this.windFactor = BigDecimal.valueOf(1.0d);
		}

		try
		{
			this.tempOffset = BigDecimal.valueOf(Double.parseDouble(PropertyWatcher.get("temperature.offset")));
		}
		catch (Exception e)
		{
			this.tempOffset = BigDecimal.valueOf(0d);
		}

		try
		{
			this.uuid = getQueryValue(PARAM_UUID);
		}
		catch (Exception e)
		{
		}

		try
		{
			this.start = getDate(getQueryValue(PARAM_START));
		}
		catch (Exception e)
		{
		}

		try
		{
			this.end = getDate(getQueryValue(PARAM_END));
		}
		catch (Exception e)
		{
		}
	}

	private synchronized Timestamp getDate(String text)
	{
		OffsetDateTime odt = OffsetDateTime.parse(text);
		Instant i = Instant.from(odt);
		Date d = Date.from(i);

		return new Timestamp(d.getTime());
	}

	@Get("json")
	public List<Measurements> getDataJson()
	{
		try (Connection conn = Database.getDirectConnection();
			 DSLContext context = Database.getContext(conn))
		{
			SelectWhereStep<?> step = context.selectFrom(MEASUREMENTS);

			if (this.start != null)
				step.where(MEASUREMENTS.CREATED.ge(this.start));
			if (this.end != null)
				step.where(MEASUREMENTS.CREATED.le(this.end));

			List<Measurements> result = step.fetchInto(Measurements.class);

			this.adjustWind(result);
			this.adjustTemp(result);

			return result;
		}
		catch (SQLException e)
		{
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
		}
	}

	private MeasurementsRecord adjustTemp(MeasurementsRecord record)
	{
		if (this.tempOffset != null && this.tempOffset.doubleValue() != 0)
		{
			// Adjust wind direction
			BigDecimal ambientTemp = record.get(MEASUREMENTS.AMBIENT_TEMP);
			BigDecimal groundTemp = record.get(MEASUREMENTS.GROUND_TEMP);

			try
			{
				if (ambientTemp != null)
					ambientTemp = ambientTemp.add(this.tempOffset);
				if (groundTemp != null)
					groundTemp = groundTemp.add(this.tempOffset);

				record.set(MEASUREMENTS.AMBIENT_TEMP, ambientTemp);
				record.set(MEASUREMENTS.GROUND_TEMP, groundTemp);
			}
			catch (Exception e)
			{
			}
		}

		return record;
	}

	private void adjustTemp(List<Measurements> result)
	{
		if (this.tempOffset != null && this.tempOffset.doubleValue() != 0)
		{
			result.forEach(r -> {
				BigDecimal ambientTemp = r.getAmbientTemp();
				BigDecimal groundTemp = r.getGroundTemp();

				if (ambientTemp != null)
				{
					ambientTemp = ambientTemp.add(this.tempOffset);
					r.setAmbientTemp(ambientTemp);
				}

				if (groundTemp != null)
				{
					groundTemp = groundTemp.add(this.tempOffset);
					r.setGroundTemp(groundTemp);
				}
			});
		}
	}

	private void adjustWind(List<Measurements> result)
	{
		if (this.windOffset != null && this.windOffset.doubleValue() != 0)
		{
			// Adjust wind direction
			BigDecimal fullCircle = BigDecimal.valueOf(360);
			result.forEach(r -> {
				BigDecimal wind = r.getWindAverage();

				if (wind != null)
				{
					wind = wind.subtract(this.windOffset);
					if (wind.doubleValue() < 0)
					{
						wind = wind.add(fullCircle);
					}
					r.setWindAverage(wind);
				}

				wind = r.getWindSpeed();

				if (wind != null)
				{
					wind = wind.multiply(this.windFactor);
					r.setWindSpeed(wind);
				}

				wind = r.getWindGust();

				if (wind != null)
				{
					wind = wind.multiply(this.windFactor);
					r.setWindGust(wind);
				}
			});
		}
	}

	private MeasurementsRecord adjustWind(MeasurementsRecord record)
	{
		if (this.windOffset != null && this.windOffset.doubleValue() != 0)
		{
			// Adjust wind direction
			BigDecimal fullCircle = BigDecimal.valueOf(360);
			BigDecimal wind = record.get(MEASUREMENTS.WIND_AVERAGE);

			if (wind != null)
			{
				wind = wind.subtract(this.windOffset);
				if (wind.doubleValue() < 0)
				{
					wind = wind.add(fullCircle);
				}
				record.set(MEASUREMENTS.WIND_AVERAGE, wind);
			}

			try
			{
				wind = record.get(MEASUREMENTS.WIND_SPEED);
				wind = wind.multiply(this.windFactor);
				record.set(MEASUREMENTS.WIND_SPEED, wind);
			}
			catch (Exception e)
			{
			}

			try
			{
				wind = record.get(MEASUREMENTS.WIND_GUST);
				wind = wind.multiply(this.windFactor);
				record.set(MEASUREMENTS.WIND_GUST, wind);
			}
			catch (Exception e)
			{
			}
		}

		return record;
	}

	@Get("txt")
	public FileRepresentation getDataPlain()
	{
		try (Connection conn = Database.getDirectConnection();
			 DSLContext context = Database.getContext(conn))
		{
			Path result = Files.createTempFile("rpi-weather", "txt");

			SelectWhereStep<MeasurementsRecord> step = context.selectFrom(MEASUREMENTS);

			if (this.start != null)
				step.where(MEASUREMENTS.CREATED.ge(this.start));
			if (this.end != null)
				step.where(MEASUREMENTS.CREATED.le(this.end));

			List<String> data = step.fetchStream()
									.map(this::adjustWind)
									.map(this::adjustTemp)
									.map(m -> MEASUREMENTS.fieldStream().map(f -> {
										Object o = m.get(f);
										if (o != null)
											return o.toString();
										else
											return "";
									}).collect(Collectors.joining("\t")))
									.collect(Collectors.toList());

			data.add(0, MEASUREMENTS.fieldStream().map(Field::getName).collect(Collectors.joining("\t")));

			Files.write(result, data);

			FileRepresentation representation = new FileRepresentation(result.toFile(), MediaType.TEXT_PLAIN);
			representation.setSize(result.toFile().length());
			representation.setDisposition(new Disposition(Disposition.TYPE_ATTACHMENT));
			representation.setAutoDeleting(true);
			return representation;
		}
		catch (SQLException | IOException e)
		{
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
		}
	}

	@Post
	public void postData(MeasurementPojo[] measurements)
	{
		if (!Objects.equals(this.uuid, PropertyWatcher.get("client.uuid")))
		{
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED);
		}
		else if (measurements == null)
		{
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
		else
		{
			try (Connection conn = Database.getDirectConnection();
				 DSLContext context = Database.getContext(conn))
			{
				Arrays.stream(measurements)
					  .filter(m -> m.getCreated() != null)
					  .map(m -> {
						  MeasurementsRecord record = context.newRecord(MEASUREMENTS);
						  record.setAmbientTemp(m.getAmbientTemp());
						  record.setGroundTemp(m.getGroundTemp());
						  record.setPressure(m.getPressure());
						  record.setHumidity(m.getHumidity());
						  record.setWindAverage(m.getWindAverage());
						  record.setWindSpeed(m.getWindSpeed());
						  record.setWindGust(m.getWindGust());
						  record.setRainfall(m.getRainfall());
						  record.setPiTemp(m.getPiTemp());
						  record.setUploadedWu(false);
						  record.setCreated(getDate(m.getCreated()));
						  return record;
					  })
					  .forEach(UpdatableRecordImpl::store);
			}
			catch (SQLException e)
			{
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
			}
		}
	}
}
