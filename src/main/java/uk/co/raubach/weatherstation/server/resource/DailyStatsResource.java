package uk.co.raubach.weatherstation.server.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.jooq.DSLContext;
import org.jooq.tools.StringUtils;
import uk.co.raubach.weatherstation.resource.DailyStats;
import uk.co.raubach.weatherstation.server.database.Database;
import uk.co.raubach.weatherstation.server.database.codegen.tables.pojos.Aggregated;

import java.io.IOException;
import java.sql.*;
import java.text.*;
import java.time.*;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

import static uk.co.raubach.weatherstation.server.database.codegen.tables.Aggregated.*;

@Path("stats/daily")
public class DailyStatsResource extends ContextResource
{
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	private synchronized Timestamp getTimestamp(String text)
	{
		if (StringUtils.isEmpty(text))
			return new Timestamp(System.currentTimeMillis());
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

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDailyStats(@QueryParam("start") String startString, @QueryParam("end") String endString)
		throws IOException, SQLException
	{
		Timestamp start = getTimestamp(startString);
		Timestamp end = getTimestamp(endString);

		try (Connection conn = Database.getDirectConnection();
			 DSLContext context = Database.getContext(conn))
		{
			List<Aggregated> statsDaily = context.selectFrom(AGGREGATED)
												 .where(AGGREGATED.DATE.ge(new java.sql.Date(start.getTime())))
												 .and(AGGREGATED.DATE.le(new java.sql.Date(end.getTime())))
												 .fetchInto(Aggregated.class);

			return Response.ok(statsDaily.stream()
										 .map(r -> {
											 DailyStats result = new DailyStats();
											 result.setDate(new Date(r.getDate().getTime()));

											 DailyStats.TypeStats min = new DailyStats.TypeStats();
											 min.setAmbientTemp(r.getMinAmbientTemp());
											 min.setGroundTemp(r.getMinGroundTemp());
											 min.setHumidity(r.getMinHumidity());
											 min.setPressure(r.getMinPressure());
											 min.setRainfall(r.getSumRainfall());
											 min.setWindAverage(r.getMinWindAverage());
											 min.setWindGust(r.getMinWindGust());
											 min.setWindSpeed(r.getMinWindSpeed());
											 DailyStats.TypeStats max = new DailyStats.TypeStats();
											 max.setAmbientTemp(r.getMaxAmbientTemp());
											 max.setGroundTemp(r.getMaxGroundTemp());
											 max.setHumidity(r.getMaxHumidity());
											 max.setPressure(r.getMaxPressure());
											 max.setRainfall(r.getSumRainfall());
											 max.setWindAverage(r.getMaxWindAverage());
											 max.setWindGust(r.getMaxWindGust());
											 max.setWindSpeed(r.getMaxWindSpeed());
											 DailyStats.TypeStats avg = new DailyStats.TypeStats();
											 avg.setAmbientTemp(r.getAvgAmbientTemp());
											 avg.setGroundTemp(r.getAvgGroundTemp());
											 avg.setHumidity(r.getAvgHumidity());
											 avg.setPressure(r.getAvgPressure());
											 avg.setRainfall(r.getSumRainfall());
											 avg.setWindAverage(r.getAvgWindAverage());
											 avg.setWindGust(r.getAvgWindGust());
											 avg.setWindSpeed(r.getAvgWindSpeed());
											 DailyStats.TypeStats std = new DailyStats.TypeStats();
											 std.setAmbientTemp(r.getStdAmbientTemp());
											 std.setGroundTemp(r.getStdGroundTemp());
											 std.setHumidity(r.getStdHumidity());
											 std.setPressure(r.getStdPressure());
											 std.setRainfall(r.getSumRainfall());
											 std.setWindAverage(r.getStdWindAverage());
											 std.setWindGust(r.getStdWindGust());
											 std.setWindSpeed(r.getStdWindSpeed());

											 result.setMin(min);
											 result.setMax(max);
											 result.setAvg(avg);
											 result.setStdv(std);

											 return result;
										 }).collect(Collectors.toList()))
						   .build();

		}
	}
}
