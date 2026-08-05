package uk.co.raubach.weatherstation.server.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import uk.co.raubach.weatherstation.resource.*;
import uk.co.raubach.weatherstation.server.database.Database;
import uk.co.raubach.weatherstation.server.database.codegen.tables.pojos.ViewPeriods;

import java.math.BigDecimal;
import java.sql.*;

import static uk.co.raubach.weatherstation.server.database.codegen.tables.Aggregated.AGGREGATED;
import static uk.co.raubach.weatherstation.server.database.codegen.tables.Measurements.MEASUREMENTS;
import static uk.co.raubach.weatherstation.server.database.codegen.tables.ViewPeriods.VIEW_PERIODS;

@Path("stats/total")
public class TotalStatsResource extends ContextResource
{
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTotal()
			throws SQLException
	{
		try (Connection conn = Database.getDirectConnection())
		{
			DSLContext context = Database.getContext(conn);
			AggregatedStats result = new AggregatedStats();
			result.setAvgTemp(context.select(DSL.avg(AGGREGATED.AVG_AMBIENT_TEMP)).from(AGGREGATED).fetchAnyInto(BigDecimal.class));
			result.setTotalRain(context.select(DSL.sum(AGGREGATED.SUM_RAINFALL)).from(AGGREGATED).fetchAnyInto(BigDecimal.class));
			result.setMostIntenseRain(context.select(MEASUREMENTS.CREATED.as("date"), MEASUREMENTS.RAINFALL.times(12).as("value"))
			                                 .from(MEASUREMENTS)
			                                 .orderBy(MEASUREMENTS.RAINFALL.desc())
			                                 .limit(1)
			                                 .fetchAnyInto(Day.class));
			result.setMostRain(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.SUM_RAINFALL.as("value"))
			                          .from(AGGREGATED)
			                          .orderBy(AGGREGATED.SUM_RAINFALL.desc())
			                          .limit(1)
			                          .fetchAnyInto(Day.class));
			result.setMostWind(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.MAX_WIND_GUST.as("value"))
			                          .from(AGGREGATED)
			                          .orderBy(AGGREGATED.MAX_WIND_GUST.desc())
			                          .limit(1)
			                          .fetchAnyInto(Day.class));
			result.setHighestTemp(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.MAX_AMBIENT_TEMP.as("value"))
			                             .from(AGGREGATED)
			                             .orderBy(AGGREGATED.MAX_AMBIENT_TEMP.desc())
			                             .limit(1)
			                             .fetchAnyInto(Day.class));
			result.setLowestTemp(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.MIN_AMBIENT_TEMP.as("value"))
			                            .from(AGGREGATED)
			                            .orderBy(AGGREGATED.MIN_AMBIENT_TEMP.asc())
			                            .limit(1)
			                            .fetchAnyInto(Day.class));

			return Response.ok(result).build();
		}
	}

	@Path("/ranked")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRankedStats()
			throws SQLException
	{
		try (Connection conn = Database.getDirectConnection())
		{
			DSLContext context = Database.getContext(conn);

			RankedStats result = new RankedStats();
			result.setLongestWetPeriod(context.selectFrom(VIEW_PERIODS).where(VIEW_PERIODS.TYPE.eq("longest_wet_period")).fetchAnyInto(ViewPeriods.class));
			result.setLongestDryPeriod(context.selectFrom(VIEW_PERIODS).where(VIEW_PERIODS.TYPE.eq("longest_dry_period")).fetchAnyInto(ViewPeriods.class));
			result.setHighestTemp(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.MAX_AMBIENT_TEMP.as("value"))
			                             .from(AGGREGATED)
			                             .orderBy(AGGREGATED.MAX_AMBIENT_TEMP.desc())
			                             .limit(10)
			                             .fetchInto(Day.class));
			result.setLowestTemp(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.MIN_AMBIENT_TEMP.as("value"))
			                            .from(AGGREGATED)
			                            .orderBy(AGGREGATED.MIN_AMBIENT_TEMP.asc())
			                            .limit(10)
			                            .fetchInto(Day.class));
			result.setHighestRain(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.SUM_RAINFALL.as("value"))
			                             .from(AGGREGATED)
			                             .orderBy(AGGREGATED.SUM_RAINFALL.desc())
			                             .limit(10)
			                             .fetchInto(Day.class));
			result.setHighestWind(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.MAX_WIND_GUST.as("value"))
			                             .from(AGGREGATED)
			                             .orderBy(AGGREGATED.MAX_WIND_GUST.desc())
			                             .limit(10)
			                             .fetchInto(Day.class));

			return Response.ok(result).build();
		}
	}
}
