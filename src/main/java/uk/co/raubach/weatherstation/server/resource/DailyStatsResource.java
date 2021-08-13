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
import java.util.Date;
import java.util.*;

public class DailyStatsResource extends ServerResource
{
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	public static final String PARAM_START = "start";
	public static final String PARAM_END   = "end";

	private String start;
	private String end;

	private double windFactor;

	@Override
	protected void doInit()
		throws ResourceException
	{
		super.doInit();

		try
		{
			getDate(getQueryValue(PARAM_START));
			this.start = getQueryValue(PARAM_START);
		}
		catch (Exception e)
		{
		}

		try
		{
			getDate(getQueryValue(PARAM_END));
			this.end = getQueryValue(PARAM_END);
		}
		catch (Exception e)
		{
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

	private synchronized Date getDate(String text)
		throws ParseException
	{
		return SDF.parse(text);
	}

	@Get
	public List<DailyStats> getDailyStats()
	{
		try (Connection conn = Database.getDirectConnection();
			 DSLContext context = Database.getContext(conn))
		{
			StatsDaily statsDaily = new StatsDaily();
			statsDaily.setStartdate(new java.sql.Date(getDate(this.start).getTime()));
			statsDaily.setStartdate(new java.sql.Date(getDate(this.end).getTime()));
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
		catch (SQLException | ParseException e)
		{
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
		}
	}
}
