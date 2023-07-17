package uk.co.raubach.weatherstation.server.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import uk.co.raubach.weatherstation.resource.AggregatedStats;
import uk.co.raubach.weatherstation.server.database.Database;

import java.math.BigDecimal;
import java.sql.*;

import static uk.co.raubach.weatherstation.server.database.codegen.tables.Aggregated.*;

@Path("stats/total")
public class TotalStatsResource extends ContextResource
{
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTotal()
		throws IndexOutOfBoundsException, SQLException
	{
		try (Connection conn = Database.getDirectConnection())
		{
			DSLContext context = Database.getContext(conn);
			AggregatedStats result = new AggregatedStats();
			result.setAvgTemp(context.select(DSL.avg(AGGREGATED.AVG_AMBIENT_TEMP)).from(AGGREGATED).fetchAnyInto(BigDecimal.class));
			result.setTotalRain(context.select(DSL.sum(AGGREGATED.SUM_RAINFALL)).from(AGGREGATED).fetchAnyInto(BigDecimal.class));
			result.setMostRain(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.SUM_RAINFALL.as("value"))
									  .from(AGGREGATED)
									  .where(AGGREGATED.SUM_RAINFALL.eq(DSL.select(DSL.max(AGGREGATED.SUM_RAINFALL)).from(AGGREGATED)))
									  .fetchAnyInto(AggregatedStats.Day.class));
			result.setMostWind(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.MAX_WIND_GUST.as("value"))
									  .from(AGGREGATED)
									  .where(AGGREGATED.MAX_WIND_GUST.eq(DSL.select(DSL.max(AGGREGATED.MAX_WIND_GUST)).from(AGGREGATED)))
									  .fetchAnyInto(AggregatedStats.Day.class));
			result.setHighestTemp(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.MAX_AMBIENT_TEMP.as("value"))
										 .from(AGGREGATED)
										 .where(AGGREGATED.MAX_AMBIENT_TEMP.eq(DSL.select(DSL.max(AGGREGATED.MAX_AMBIENT_TEMP)).from(AGGREGATED)))
										 .fetchAnyInto(AggregatedStats.Day.class));
			result.setLowestTemp(context.select(AGGREGATED.DATE.as("date"), AGGREGATED.MIN_AMBIENT_TEMP.as("value"))
										.from(AGGREGATED)
										.where(AGGREGATED.MIN_AMBIENT_TEMP.eq(DSL.select(DSL.min(AGGREGATED.MIN_AMBIENT_TEMP)).from(AGGREGATED)))
										.fetchAnyInto(AggregatedStats.Day.class));

			return Response.ok(result).build();
		}
	}
}
