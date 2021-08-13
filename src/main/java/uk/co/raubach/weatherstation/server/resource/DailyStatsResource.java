package uk.co.raubach.weatherstation.server.resource;

import org.jooq.DSLContext;
import org.restlet.data.Status;
import org.restlet.resource.*;
import uk.co.raubach.weatherstation.resource.DailyStats;
import uk.co.raubach.weatherstation.server.database.Database;
import uk.co.raubach.weatherstation.server.database.codegen.routines.StatsDaily;
import uk.co.raubach.weatherstation.server.util.PropertyWatcher;

import java.sql.*;
import java.text.*;
import java.time.*;
import java.util.Date;
import java.util.*;

public class DailyStatsResource extends ServerResource
{
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	public static final String PARAM_START = "start";
	public static final String PARAM_END   = "end";

	private Timestamp start;
	private Timestamp end;

	private double windFactor;

	@Override
	protected void doInit()
		throws ResourceException
	{
		super.doInit();

		try
		{
			this.start = getTimestamp(getQueryValue(PARAM_START));
		}
		catch (Exception e)
		{
			this.start = new Timestamp(System.currentTimeMillis());
		}

		try
		{
			this.end = getTimestamp(getQueryValue(PARAM_END));
		}
		catch (Exception e)
		{
			this.end = new Timestamp(System.currentTimeMillis());
		}

		try
		{
			this.windFactor = Double.parseDouble(PropertyWatcher.get("wind.strength.multiplier"));
		}
		catch (Exception e)
		{
			this.windFactor = 1d;
		}
	}

	private synchronized Timestamp getTimestamp(String text)
	{
		OffsetDateTime odt = OffsetDateTime.parse(text);
		Instant i = Instant.from(odt);
		Date d = Date.from(i);

		return new Timestamp(d.getTime());
	}

	private synchronized Date getDate(String text)
	{
		try
		{
			return SDF.parse(text);
		}
		catch (ParseException e)
		{
			return null;
		}
	}

	@Get
	public List<DailyStats> getDailyStats()
	{
		try (Connection conn = Database.getDirectConnection();
			 DSLContext context = Database.getContext(conn))
		{
			StatsDaily statsDaily = new StatsDaily();
			statsDaily.setStartdate(this.start);
			statsDaily.setEnddate(this.end);
			statsDaily.setWindfactor(this.windFactor);
			statsDaily.execute(context.configuration());

			Map<String, DailyStats> tempMap = new TreeMap<>();

			statsDaily.getResults()
					  .get(0)
					  .forEach(r -> {
						  String date = r.get("date", String.class);
						  String agrType = r.get("agrType", String.class);

						  DailyStats existing = tempMap.get(date);

						  if (existing == null)
						  {
							  existing = new DailyStats();
							  try
							  {
								  existing.setDate(getDate(date));
							  }
							  catch (Exception e)
							  {
							  }
						  }

						  switch (agrType)
						  {
							  case "avg":
								  existing.setAvg(new DailyStats.TypeStats(r));
								  break;
							  case "min":
								  existing.setMin(new DailyStats.TypeStats(r));
								  break;
							  case "max":
								  existing.setMax(new DailyStats.TypeStats(r));
								  break;
							  case "stdv":
								  existing.setStdv(new DailyStats.TypeStats(r));
								  break;
						  }

						  tempMap.put(date, existing);
					  });

			return new ArrayList<>(tempMap.values());
		}
		catch (SQLException e)
		{
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
		}
	}
}
