package uk.co.raubach.weatherstation.server.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.jooq.*;
import org.jooq.impl.DSL;
import uk.co.raubach.weatherstation.resource.WeeklyStats;
import uk.co.raubach.weatherstation.server.database.Database;

import java.math.BigDecimal;
import java.sql.*;

import static uk.co.raubach.weatherstation.server.database.codegen.tables.Aggregated.*;

@Path("stats/weekly")
public class WeekStatsResource extends ContextResource
{
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWeekly()
		throws IndexOutOfBoundsException, SQLException
	{
		try (Connection conn = Database.getDirectConnection();
			 DSLContext context = Database.getContext(conn))
		{
			Condition condition = DSL.abs(DSL.dateDiff(AGGREGATED.DATE, DSL.currentDate())).le(7);

			WeeklyStats result = new WeeklyStats();
			result.setAvgTemp(context.select(DSL.avg(AGGREGATED.AVG_AMBIENT_TEMP)).from(AGGREGATED).where(condition).fetchAnyInto(BigDecimal.class));
			result.setTotalRain(context.select(DSL.sum(AGGREGATED.SUM_RAINFALL)).from(AGGREGATED).where(condition).fetchAnyInto(BigDecimal.class));
			result.setMostRain(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.SUM_RAINFALL.as("value"))
									  .from(AGGREGATED)
									  .where(condition)
									  .and(AGGREGATED.SUM_RAINFALL.eq(DSL.select(DSL.max(AGGREGATED.SUM_RAINFALL)).from(AGGREGATED).where(condition)))
									  .fetchAnyInto(WeeklyStats.Day.class));
			result.setMostWind(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.MAX_WIND_GUST.as("value"))
									  .from(AGGREGATED)
									  .where(condition)
									  .and(AGGREGATED.MAX_WIND_GUST.eq(DSL.select(DSL.max(AGGREGATED.MAX_WIND_GUST)).from(AGGREGATED).where(condition)))
									  .fetchAnyInto(WeeklyStats.Day.class));
			result.setHighestTemp(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.MAX_AMBIENT_TEMP.as("value"))
										 .from(AGGREGATED)
										 .where(condition)
										 .and(AGGREGATED.MAX_AMBIENT_TEMP.eq(DSL.select(DSL.max(AGGREGATED.MAX_AMBIENT_TEMP)).from(AGGREGATED).where(condition)))
										 .fetchAnyInto(WeeklyStats.Day.class));
			result.setLowestTemp(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.MIN_AMBIENT_TEMP.as("value"))
										.from(AGGREGATED)
										.where(condition)
										.and(AGGREGATED.MIN_AMBIENT_TEMP.eq(DSL.select(DSL.min(AGGREGATED.MIN_AMBIENT_TEMP)).from(AGGREGATED).where(condition)))
										.fetchAnyInto(WeeklyStats.Day.class));

			return Response.ok(result).build();
		}
	}
}
