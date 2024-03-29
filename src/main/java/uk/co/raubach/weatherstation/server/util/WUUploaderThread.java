package uk.co.raubach.weatherstation.server.util;

import okhttp3.*;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.tools.StringUtils;
import uk.co.raubach.weatherstation.server.database.Database;
import uk.co.raubach.weatherstation.server.database.codegen.tables.Measurements;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import static uk.co.raubach.weatherstation.server.database.codegen.tables.Measurements.*;

public class WUUploaderThread implements Runnable
{
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private OkHttpClient client;
	private BigDecimal   windOffset;
	private BigDecimal   windFactor;
	private BigDecimal   tempOffset;

	public WUUploaderThread()
	{
		this.client = new OkHttpClient();

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

	@Override
	public void run()
	{
		String url = PropertyWatcher.get("wu.url");
		String id = PropertyWatcher.get("wu.id");
		String password = PropertyWatcher.get("wu.password");

		if (StringUtils.isEmpty(url) || StringUtils.isEmpty(id) || StringUtils.isEmpty(password))
			return;

		try (Connection conn = Database.getDirectConnection())
		{
			DSLContext context = Database.getContext(conn);
			Measurements m = MEASUREMENTS.as("m");

			Field<BigDecimal> lastSixty = DSL.select(DSL.sum(m.RAINFALL)).from(m).where(timestampDiff(DatePart.MINUTE, MEASUREMENTS.CREATED, m.CREATED).between(0, 60)).asField("last_sixty");
			Field<BigDecimal> sinceMidnight = DSL.select(DSL.sum(m.RAINFALL)).from(m).where(timestampDiff(DatePart.MINUTE, DSL.timestamp(DSL.date(MEASUREMENTS.CREATED)), m.CREATED).gt(0)).and(timestampDiff(DatePart.MINUTE, MEASUREMENTS.CREATED, m.CREATED).le(0)).asField("since_midnight");

			List<Field<?>> fields = new ArrayList<>();
			fields.addAll(Arrays.asList(MEASUREMENTS.fields()));
			fields.add(lastSixty);
			fields.add(sinceMidnight);
			SelectConditionStep<?> step = context.select(fields)
												 .from(MEASUREMENTS)
												 .where(MEASUREMENTS.UPLOADED_WU.eq(false));
			step.stream()
				.forEach(r -> {
					try
					{
						HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder()
														 .addQueryParameter("ID", id)
														 .addQueryParameter("PASSWORD", password)
														 .addQueryParameter("dateutc", sdf.format(r.get(MEASUREMENTS.CREATED)))
														 .addQueryParameter("action", "updateraw");

						BigDecimal ambientTemp = r.get(MEASUREMENTS.AMBIENT_TEMP);
						if (ambientTemp != null)
						{
							ambientTemp = ambientTemp.add(tempOffset);
							builder.addQueryParameter("tempf", Double.toString(ambientTemp.doubleValue() * (9 / 5.0) + 32));
						}

						BigDecimal humidity = r.get(MEASUREMENTS.HUMIDITY);
						if (humidity != null)
						{
							builder.addQueryParameter("humidity", Double.toString(humidity.doubleValue()));
						}

						BigDecimal wind = r.get(MEASUREMENTS.WIND_AVERAGE);
						if (wind != null)
						{
							double value = wind.subtract(windOffset).doubleValue();

							if (value < 0)
							{
								value += 360;
							}

							builder.addQueryParameter("winddir", Double.toString(value));
						}

						BigDecimal windSpeed = r.get(MEASUREMENTS.WIND_SPEED);
						if (windSpeed != null)
						{
							windSpeed = windSpeed.multiply(windFactor);
							builder.addQueryParameter("windspeedmph", Double.toString(windSpeed.doubleValue() * 0.621371));
						}

						BigDecimal windGust = r.get(MEASUREMENTS.WIND_GUST);
						if (windGust != null)
						{
							windGust = windGust.multiply(windFactor);
							builder.addQueryParameter("windgustmph", Double.toString(windGust.doubleValue() * 0.621371));
						}

						BigDecimal rainfallLastSixty = r.get(lastSixty);
						BigDecimal rainfallSinceMidnight = r.get(sinceMidnight);
						if (rainfallLastSixty != null && rainfallSinceMidnight != null)
						{
							builder.addQueryParameter("rainin", Double.toString(rainfallLastSixty.doubleValue() * 0.0393701));
							builder.addQueryParameter("dailyrainin", Double.toString(rainfallSinceMidnight.doubleValue() * 0.0393701));
						}

						BigDecimal pressure = r.get(MEASUREMENTS.PRESSURE);
						if (pressure != null)
						{
							builder.addQueryParameter("baromin", Double.toString(pressure.doubleValue() * 0.02953));
						}

						if (ambientTemp != null && humidity != null)
						{
							double temp = ambientTemp.doubleValue();
							double hum = humidity.doubleValue();
							double dewPoint = (temp - (14.55 + 0.114 * temp) * (1 - (0.01 * hum)) - Math.pow(((2.5 + 0.007 * temp) * (1 - (0.01 * hum))), 3) - (15.9 + 0.117 * temp) * Math.pow((1 - (0.01 * hum)), 14));

							dewPoint = dewPoint * (9 / 5.0) + 32;

							builder.addQueryParameter("dewptf", Double.toString(dewPoint));
						}

						HttpUrl builtUrl = builder.build();

						Request request = new Request.Builder()
							.get()
							.url(builtUrl)
							.build();

						try (Response response = client.newCall(request).execute())
						{
							if (response.isSuccessful())
							{
								context.update(MEASUREMENTS)
									   .set(MEASUREMENTS.UPLOADED_WU, true)
									   .where(MEASUREMENTS.ID.eq(r.get(MEASUREMENTS.ID)))
									   .execute();
							}
							else
							{
								Logger.getLogger("").warning(response.message());
							}
						}
					}
					catch (Exception e)
					{
						Logger.getLogger("").severe(e.getMessage());
						e.printStackTrace();
					}
				});
		}
		catch (SQLException e)
		{
			Logger.getLogger("").severe(e.getMessage());
			e.printStackTrace();
		}
	}

	private static Field<Integer> timestampDiff(DatePart part, Field<Timestamp> t1, Field<Timestamp> t2)
	{
		return DSL.field("timestampdiff({0}, {1}, {2})",
			Integer.class, DSL.keyword(part.toSQL()), t1, t2);
	}
}
