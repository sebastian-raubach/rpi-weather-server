package uk.co.raubach.weatherstation.server.util;

import okhttp3.*;
import org.jooq.DSLContext;
import org.jooq.tools.StringUtils;
import uk.co.raubach.weatherstation.server.database.Database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import static uk.co.raubach.weatherstation.server.database.codegen.tables.Measurements.*;

public class WUUploaderThread implements Runnable
{
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private OkHttpClient client;

	public WUUploaderThread()
	{
		this.client = new OkHttpClient();
	}

	@Override
	public void run()
	{
		String url = PropertyWatcher.get("wu.url");
		String id = PropertyWatcher.get("wu.id");
		String password = PropertyWatcher.get("wu.password");

		double windOffset = 0;

		try
		{
			windOffset = Double.parseDouble(PropertyWatcher.get("wind.direction.offset"));
		}
		catch (Exception e)
		{
		}

		final double finalWindOffset = windOffset;

		if (StringUtils.isEmpty(url) || StringUtils.isEmpty(id) || StringUtils.isEmpty(password))
			return;

		try (Connection conn = Database.getDirectConnection();
			 DSLContext context = Database.getContext(conn))
		{
			context.selectFrom(MEASUREMENTS)
				   .where(MEASUREMENTS.UPLOADED_WU.eq(false))
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
							   double value = wind.doubleValue() - finalWindOffset;

							   if (value < 0)
							   {
								   value += 360;
							   }

							   builder.addQueryParameter("winddir", Double.toString(value));
						   }

						   BigDecimal windSpeed = r.get(MEASUREMENTS.WIND_SPEED);
						   if (windSpeed != null)
						   {
							   builder.addQueryParameter("windspeedmph", Double.toString(windSpeed.doubleValue() * 0.621371));
						   }

						   BigDecimal windGust = r.get(MEASUREMENTS.WIND_GUST);
						   if (windGust != null)
						   {
							   builder.addQueryParameter("windgustmph", Double.toString(windGust.doubleValue() * 0.621371));
						   }

						   BigDecimal rainfall = r.get(MEASUREMENTS.RAINFALL);
						   if (rainfall != null)
						   {
							   builder.addQueryParameter("rainin", Double.toString(rainfall.doubleValue() * 0.0393701));
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

						   Logger.getLogger("").info("SENDING DATA TO WU: " + request.toString());

						   try (Response response = client.newCall(request).execute())
						   {
							   if (response.isSuccessful())
							   {
								   r.setUploadedWu(true);
								   r.store(MEASUREMENTS.UPLOADED_WU);
							   }
							   else
							   {
								   Logger.getLogger("").warning(response.message());
							   }
						   }
					   }
					   catch (Exception e)
					   {
						   e.printStackTrace();
					   }
				   });
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
