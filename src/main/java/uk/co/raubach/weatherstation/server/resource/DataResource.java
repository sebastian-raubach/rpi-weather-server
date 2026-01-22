package uk.co.raubach.weatherstation.server.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.*;
import org.jooq.*;
import org.jooq.impl.*;
import org.jooq.tools.StringUtils;
import uk.co.raubach.weatherstation.resource.*;
import uk.co.raubach.weatherstation.server.database.Database;
import uk.co.raubach.weatherstation.server.database.codegen.tables.records.MeasurementsRecord;
import uk.co.raubach.weatherstation.server.util.*;

import java.io.IOException;
import java.math.*;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

import static uk.co.raubach.weatherstation.server.database.codegen.tables.Measurements.MEASUREMENTS;

@jakarta.ws.rs.Path("data")
public class DataResource extends ContextResource
{
	private static BigDecimal windOffset;
	private static BigDecimal windFactor;
	private static BigDecimal tempOffset;

	static
	{
		try
		{
			windOffset = BigDecimal.valueOf(Double.parseDouble(PropertyWatcher.get("wind.direction.offset")));
		}
		catch (Exception e)
		{
			windOffset = BigDecimal.valueOf(0.0d);
		}

		try
		{
			windFactor = BigDecimal.valueOf(Double.parseDouble(PropertyWatcher.get("wind.strength.multiplier")));
		}
		catch (Exception e)
		{
			windFactor = BigDecimal.valueOf(1.0d);
		}

		try
		{
			tempOffset = BigDecimal.valueOf(Double.parseDouble(PropertyWatcher.get("temperature.offset")));
		}
		catch (Exception e)
		{
			tempOffset = BigDecimal.valueOf(0d);
		}
	}

	private synchronized Timestamp getDate(String text)
	{
		if (StringUtils.isEmpty(text))
			return null;

		OffsetDateTime odt = OffsetDateTime.parse(text);
		Instant i = Instant.from(odt);
		Date d = Date.from(i);

		return new Timestamp(d.getTime());
	}

	@DELETE
	@Path("/rainfall")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRainfallDataForTimeframe(RainfallDeleteRequest request)
			throws SQLException
	{
		if (request == null)
			return Response.status(Response.Status.BAD_REQUEST).build();

		if (!Objects.equals(request.getUuid(), PropertyWatcher.get("client.uuid")))
			return Response.status(Response.Status.UNAUTHORIZED).build();
		else
		{
			Timestamp start = getDate(request.getStart());
			Timestamp end = getDate(request.getEnd());

			if (start == null || end == null)
				return Response.status(Response.Status.BAD_REQUEST).build();

			try (Connection conn = Database.getDirectConnection())
			{
				DSLContext context = Database.getContext(conn);

				context.update(MEASUREMENTS).set(MEASUREMENTS.RAINFALL, BigDecimal.valueOf(0))
					   .where(MEASUREMENTS.CREATED.ge(start))
					   .and(MEASUREMENTS.CREATED.le(end))
					   .execute();

				return Response.ok().build();
			}
		}
	}

	@GET
	@Path("/forecast")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDataForecast(@QueryParam("start") String startString, @QueryParam("end") String endString)
	{
		Timestamp start = getDate(startString);
		Timestamp end = getDate(endString);

		if (start != null && end != null && ForecastThread.FORECAST != null)
		{
			return Response.ok(ForecastThread.FORECAST.stream()
													  .filter(t -> t.getCreated().getTime() >= start.getTime() && t.getCreated().getTime() <= end.getTime())
													  .sorted((a, b) -> (int) Math.signum(a.getCreated().getTime() - b.getCreated().getTime()))
													  .collect(Collectors.toList()))
						   .build();
		}
		else
		{
			return Response.noContent().build();
		}
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDataJson(@QueryParam("start") String startString, @QueryParam("end") String endString)
			throws IOException, SQLException
	{
		Timestamp start = getDate(startString);
		Timestamp end = getDate(endString);

		try (Connection conn = Database.getDirectConnection())
		{
			DSLContext context = Database.getContext(conn);
			SelectWhereStep<?> step = context.selectFrom(MEASUREMENTS);

			if (start != null)
				step.where(MEASUREMENTS.CREATED.ge(start));
			if (end != null)
				step.where(MEASUREMENTS.CREATED.le(end));

			List<ExtendedMeasurement> result = step.fetchInto(ExtendedMeasurement.class);

			adjustWind(result);
			adjustTemp(result);
			calculateFeelsLike(result);

			return Response.ok(result).build();
		}
	}

	public static void calculateFeelsLike(List<ExtendedMeasurement> result)
	{
		final double c1 = -8.784_694_755_56;
		final double c2 = 1.611_394_11;
		final double c3 = 2.338_548_838_89;
		final double c4 = -0.146_116_05;
		final double c5 = -0.012_308_094;
		final double c6 = -0.016_424_827_7778;
		final double c7 = 2.211_732 * Math.pow(10, -3);
		final double c8 = 7.2546 * Math.pow(10, -4);
		final double c9 = -3.582 * Math.pow(10, -6);

		result.forEach(rec -> {
			double t = rec.getAmbientTemp().doubleValue();
			double r = rec.getHumidity().doubleValue();
			double w = rec.getWindSpeed().doubleValue();

			if (t >= 20)
			{
				double tt = Math.pow(t, 2);
				double rr = Math.pow(r, 2);

				double d = c1
						+ c2 * t
						+ c3 * r
						+ c4 * t * r
						+ c5 * tt
						+ c6 * rr
						+ c7 * tt * r
						+ c8 * t * rr
						+ c9 * tt * rr;

				BigDecimal dec = new BigDecimal(d, MathContext.DECIMAL64);
				dec = dec.setScale(10, RoundingMode.HALF_UP);

				rec.setHeatIndex(dec);
			}
			else if (t <= 10 && w != 0)
			{
				double d = 13.12 + 0.6215 * t + (0.3965 * t - 11.37) * Math.pow(w, 0.16);

				BigDecimal dec = new BigDecimal(d, MathContext.DECIMAL64);
				dec = dec.setScale(10, RoundingMode.HALF_UP);

				rec.setHeatIndex(dec);
			}
			else
			{
				rec.setHeatIndex(rec.getAmbientTemp());
			}
		});
	}

	public static void adjustTemp(List<ExtendedMeasurement> result)
	{
		if (tempOffset != null && tempOffset.doubleValue() != 0)
		{
			result.forEach(r -> {
				BigDecimal ambientTemp = r.getAmbientTemp();
				BigDecimal groundTemp = r.getGroundTemp();

				if (ambientTemp != null)
				{
					ambientTemp = ambientTemp.add(tempOffset);
					r.setAmbientTemp(ambientTemp);
				}

				if (groundTemp != null)
				{
					groundTemp = groundTemp.add(tempOffset);
					r.setGroundTemp(groundTemp);
				}
			});
		}
	}

	public static void adjustWind(List<ExtendedMeasurement> result)
	{
		if (windOffset != null && windOffset.doubleValue() != 0)
		{
			// Adjust wind direction
			BigDecimal fullCircle = BigDecimal.valueOf(360);
			result.forEach(r -> {
				BigDecimal wind = r.getWindAverage();
				if (wind != null)
				{
					wind = wind.subtract(windOffset);
					if (wind.doubleValue() < 0)
					{
						wind = wind.add(fullCircle);
					}
					r.setWindAverage(wind);
				}
			});
		}

		if (windFactor != null && windFactor.doubleValue() != 1)
		{
			result.forEach(r -> {
				BigDecimal wind = r.getWindSpeed();
				if (wind != null)
				{
					wind = wind.multiply(windFactor);
					r.setWindSpeed(wind);
				}

				wind = r.getWindGust();
				if (wind != null)
				{
					wind = wind.multiply(windFactor);
					r.setWindGust(wind);
				}
			});
		}
	}

//	@Get("txt")
//	public FileRepresentation getDataPlain()
//	{
//		try (Connection conn = Database.getDirectConnection();
//			 DSLContext context = Database.getContext(conn))
//		{
//			Path result = Files.createTempFile("rpi-weather", "txt");
//
//			SelectWhereStep<MeasurementsRecord> step = context.selectFrom(MEASUREMENTS);
//
//			if (start != null)
//				step.where(MEASUREMENTS.CREATED.ge(start));
//			if (end != null)
//				step.where(MEASUREMENTS.CREATED.le(end));
//
//			List<String> data = step.fetchStream()
//									.map(this::adjustWind)
//									.map(this::adjustTemp)
//									.map(m -> MEASUREMENTS.fieldStream().map(f -> {
//										Object o = m.get(f);
//										if (o != null)
//											return o.toString();
//										else
//											return "";
//									}).collect(Collectors.joining("\t")))
//									.collect(Collectors.toList());
//
//			data.add(0, MEASUREMENTS.fieldStream().map(Field::getName).collect(Collectors.joining("\t")));
//
//			Files.write(result, data);
//
//			FileRepresentation representation = new FileRepresentation(result.toFile(), MediaType.TEXT_PLAIN);
//			representation.setSize(result.toFile().length());
//			representation.setDisposition(new Disposition(Disposition.TYPE_ATTACHMENT));
//			representation.setAutoDeleting(true);
//			return representation;
//		}
//	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postData(MeasurementPojo[] measurements, @QueryParam("uuid") String uuid)
			throws IOException, SQLException
	{
		if (!Objects.equals(uuid, PropertyWatcher.get("client.uuid")))
			return Response.status(Response.Status.UNAUTHORIZED).build();
		else if (measurements == null)
			return Response.status(Response.Status.BAD_REQUEST).build();
		else
		{
			try (Connection conn = Database.getDirectConnection())
			{
				DSLContext context = Database.getContext(conn);
				Arrays.stream(measurements)
					  .filter(m -> m.getCreated() != null)
					  .map(m -> {
						  if (m.getLoftTemp() != null)
						  {
							  MeasurementsRecord record = context.selectFrom(MEASUREMENTS).where(DSL.timestampDiff(DatePart.MINUTE, MEASUREMENTS.CREATED, getDate(m.getCreated())).lt(10))
																 .and(MEASUREMENTS.CREATED.le(getDate(m.getCreated())))
																 .orderBy(MEASUREMENTS.CREATED.desc())
																 .fetchAny();

							  if (record != null)
							  {
								  record.setLoftTemp(m.getLoftTemp());
								  record.setLoftHumidity(m.getLoftHumidity());
								  return record;
							  }
							  else
							  {
								  return null;
							  }
						  }
						  else
						  {
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
							  record.setLux(m.getLux());
							  record.setLoftTemp(m.getLoftTemp());
							  record.setLoftHumidity(m.getLoftHumidity());
							  record.setUploadedWu(false);
							  record.setCreated(getDate(m.getCreated()));
							  return record;
						  }
					  })
					  .filter(Objects::nonNull)
					  .forEach(UpdatableRecordImpl::store);
			}
		}

		return Response.noContent().build();
	}
}
