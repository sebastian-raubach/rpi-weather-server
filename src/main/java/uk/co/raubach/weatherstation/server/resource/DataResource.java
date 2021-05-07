package uk.co.raubach.weatherstation.server.resource;

import org.jooq.*;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.data.Status;
import org.restlet.data.*;
import org.restlet.representation.FileRepresentation;
import org.restlet.resource.*;
import uk.co.raubach.weatherstation.server.database.Database;
import uk.co.raubach.weatherstation.server.database.codegen.tables.pojos.Measurements;
import uk.co.raubach.weatherstation.server.util.PropertyWatcher;

import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
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

	@Override
	protected void doInit()
		throws ResourceException
	{
		super.doInit();

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
		TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(text);
		Instant i = Instant.from(ta);
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

			return step.fetchInto(Measurements.class);
		}
		catch (SQLException e)
		{
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
		}
	}

	@Get("txt")
	public FileRepresentation getDataPlain()
	{
		try (Connection conn = Database.getDirectConnection();
			 DSLContext context = Database.getContext(conn))
		{
			Path result = Files.createTempFile("rpi-weather", "txt");

			SelectWhereStep<?> step = context.selectFrom(MEASUREMENTS);

			if (this.start != null)
				step.where(MEASUREMENTS.CREATED.ge(this.start));
			if (this.end != null)
				step.where(MEASUREMENTS.CREATED.le(this.end));

			List<String> data = step.fetchStream()
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
			return representation;
		}
		catch (SQLException | IOException e)
		{
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
		}
	}

	@Post
	public void postData(Measurements[] measurements)
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
					  .map(m -> context.newRecord(MEASUREMENTS, m))
					  .forEach(UpdatableRecordImpl::store);
			}
			catch (SQLException e)
			{
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
			}
		}
	}
}
