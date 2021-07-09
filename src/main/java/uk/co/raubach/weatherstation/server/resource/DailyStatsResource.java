package uk.co.raubach.weatherstation.server.resource;

import org.jooq.*;
import org.jooq.tools.StringUtils;
import org.restlet.data.Status;
import org.restlet.resource.*;
import uk.co.raubach.weatherstation.resource.DailyStats;
import uk.co.raubach.weatherstation.server.database.Database;
import uk.co.raubach.weatherstation.server.database.codegen.tables.pojos.ViewStatsDaily;

import java.sql.*;
import java.text.*;
import java.util.Date;
import java.util.*;

import static uk.co.raubach.weatherstation.server.database.codegen.tables.ViewStatsDaily.*;

public class DailyStatsResource extends ServerResource
{
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	public static final String PARAM_START = "start";
	public static final String PARAM_END   = "end";

	private String start;
	private String end;

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
			SelectWhereStep<?> step = context.selectFrom(VIEW_STATS_DAILY);

			if (!StringUtils.isEmpty(this.start))
				step.where(VIEW_STATS_DAILY.DATE.ge(this.start));
			if (!StringUtils.isEmpty(this.end))
				step.where(VIEW_STATS_DAILY.DATE.le(this.end));

			List<ViewStatsDaily> stats = step.fetchInto(ViewStatsDaily.class);

			Map<String, DailyStats> tempMap = new TreeMap<>();

			stats.forEach(s -> {
				DailyStats existing = tempMap.get(s.getDate());

				if (existing == null)
				{
					existing = new DailyStats();
					try
					{
						existing.setDate(getDate(s.getDate()));
					}
					catch (Exception e)
					{
					}
				}

				switch (s.getAgrtype())
				{
					case "avg":
						existing.setAvg(new DailyStats.TypeStats(s));
						break;
					case "min":
						existing.setMin(new DailyStats.TypeStats(s));
						break;
					case "max":
						existing.setMax(new DailyStats.TypeStats(s));
						break;
					case "stdv":
						existing.setStdv(new DailyStats.TypeStats(s));
						break;
				}

				tempMap.put(s.getDate(), existing);
			});

			return new ArrayList<>(tempMap.values());
		}
		catch (SQLException e)
		{
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
		}
	}
}
