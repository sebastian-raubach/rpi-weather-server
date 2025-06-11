package uk.co.raubach.weatherstation.server.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.jooq.*;
import org.jooq.impl.DSL;
import uk.co.raubach.weatherstation.resource.AggregatedStats;
import uk.co.raubach.weatherstation.server.database.Database;
import uk.co.raubach.weatherstation.server.database.codegen.tables.pojos.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.*;
import java.util.*;

import static uk.co.raubach.weatherstation.server.database.codegen.tables.Aggregated.AGGREGATED;
import static uk.co.raubach.weatherstation.server.database.codegen.tables.AggregatedYearMonth.AGGREGATED_YEAR_MONTH;
import static uk.co.raubach.weatherstation.server.database.codegen.tables.Measurements.MEASUREMENTS;

@Path("stats/monthly")
public class MonthStatsResource extends ContextResource
{
	@GET
	@Path("/{month:\\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMonthStats(@PathParam("month") Integer month)
			throws SQLException
	{
		if (month == null)
			return Response.status(Response.Status.BAD_REQUEST).build();

		try (Connection conn = Database.getDirectConnection())
		{
			DSLContext context = Database.getContext(conn);

			return Response.ok(context.selectFrom(AGGREGATED_YEAR_MONTH)
									  .where(AGGREGATED_YEAR_MONTH.MONTH.eq(month.shortValue()))
									  .fetchInto(AggregatedYearMonth.class)).build();
		}
	}

	@GET
	@Path("/measurements")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDataJson(@QueryParam("month") Integer month)
			throws SQLException
	{
		Map<Integer, List<Aggregated>> result = new HashMap<>();

		try (Connection conn = Database.getDirectConnection())
		{
			DSLContext context = Database.getContext(conn);

			List<Integer> years = context.selectDistinct(DSL.year(MEASUREMENTS.CREATED)).from(MEASUREMENTS).fetchInto(Integer.class);

			if (month == null)
				month = ZonedDateTime.now(ZoneOffset.UTC).getMonthValue();

			for (Integer year : years)
			{
				Condition condition = DSL.month(AGGREGATED.DATE).eq(month).and(DSL.year(AGGREGATED.DATE).eq(year));

				List<Aggregated> yearData = context.selectFrom(AGGREGATED)
												   .where(condition)
												   .fetchInto(Aggregated.class);

				result.put(year, yearData);
			}
		}

		return Response.ok(result).build();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMonthly(@QueryParam("year") Integer year, @QueryParam("month") Integer month)
			throws IndexOutOfBoundsException, SQLException
	{
		try (Connection conn = Database.getDirectConnection())
		{
			DSLContext context = Database.getContext(conn);

			if (month == null)
				month = ZonedDateTime.now(ZoneOffset.UTC).getMonthValue();
			if (year == null)
				year = ZonedDateTime.now(ZoneOffset.UTC).getYear();
			Condition condition = DSL.month(AGGREGATED.DATE).eq(month).and(DSL.year(AGGREGATED.DATE).eq(year));

			AggregatedStats result = new AggregatedStats();
			result.setAvgTemp(context.select(DSL.avg(AGGREGATED.AVG_AMBIENT_TEMP)).from(AGGREGATED).where(condition).fetchAnyInto(BigDecimal.class));
			result.setTotalRain(context.select(DSL.sum(AGGREGATED.SUM_RAINFALL)).from(AGGREGATED).where(condition).fetchAnyInto(BigDecimal.class));
			result.setMostRain(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.SUM_RAINFALL.as("value"))
									  .from(AGGREGATED)
									  .where(condition)
									  .and(AGGREGATED.SUM_RAINFALL.eq(DSL.select(DSL.max(AGGREGATED.SUM_RAINFALL)).from(AGGREGATED).where(condition)))
									  .fetchAnyInto(AggregatedStats.Day.class));
			result.setMostWind(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.MAX_WIND_GUST.as("value"))
									  .from(AGGREGATED)
									  .where(condition)
									  .and(AGGREGATED.MAX_WIND_GUST.eq(DSL.select(DSL.max(AGGREGATED.MAX_WIND_GUST)).from(AGGREGATED).where(condition)))
									  .fetchAnyInto(AggregatedStats.Day.class));
			result.setHighestTemp(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.MAX_AMBIENT_TEMP.as("value"))
										 .from(AGGREGATED)
										 .where(condition)
										 .and(AGGREGATED.MAX_AMBIENT_TEMP.eq(DSL.select(DSL.max(AGGREGATED.MAX_AMBIENT_TEMP)).from(AGGREGATED).where(condition)))
										 .fetchAnyInto(AggregatedStats.Day.class));
			result.setLowestTemp(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.MIN_AMBIENT_TEMP.as("value"))
										.from(AGGREGATED)
										.where(condition)
										.and(AGGREGATED.MIN_AMBIENT_TEMP.eq(DSL.select(DSL.min(AGGREGATED.MIN_AMBIENT_TEMP)).from(AGGREGATED).where(condition)))
										.fetchAnyInto(AggregatedStats.Day.class));

			return Response.ok(result).build();
		}
	}
}
