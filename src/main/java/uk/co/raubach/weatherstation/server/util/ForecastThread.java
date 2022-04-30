package uk.co.raubach.weatherstation.server.util;

import com.google.gson.Gson;
import okhttp3.*;
import uk.co.raubach.weatherstation.resource.OpenWeathermapForecast;
import uk.co.raubach.weatherstation.server.database.codegen.tables.pojos.Measurements;

import java.io.IOException;
import java.math.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Logger;

public class ForecastThread implements Runnable
{
	public static List<Measurements> FORECAST;

	@Override
	public void run()
	{
		OkHttpClient client = new OkHttpClient();

		HttpUrl.Builder builder = HttpUrl.parse("https://api.openweathermap.org/data/2.5/onecall").newBuilder()
										 .addQueryParameter("exclude", "daily")
										 .addQueryParameter("exclude", "alerts")
										 .addQueryParameter("lat", PropertyWatcher.get("latitude"))
										 .addQueryParameter("lon", PropertyWatcher.get("longitude"))
										 .addQueryParameter("appid", PropertyWatcher.get("openweathermap.key"))
										 .addQueryParameter("units", "metric");

		HttpUrl builtUrl = builder.build();

		Request request = new Request.Builder()
			.get()
			.url(builtUrl)
			.build();

		Logger.getLogger("").info("SENDING REQUEST TO: " + request.toString());

		try
		{
			try (Response response = client.newCall(request).execute())
			{
				if (response.isSuccessful())
				{
					Gson gson = new Gson();
					OpenWeathermapForecast data = gson.fromJson(response.body().string(), OpenWeathermapForecast.class);

					List<Measurements> measurements = new ArrayList<>();
					data.minutely().forEach(h -> {
						Measurements m = new Measurements();
						m.setAmbientTemp(h.temp());
						m.setHumidity(h.humidity());
						m.setRainfall(h.precipitation());
						m.setPressure(h.pressure());
						m.setHumidity(h.humidity());
						m.setWindGust(h.windGust() == null ? null : h.windGust().multiply(BigDecimal.valueOf(18)).divide(BigDecimal.valueOf(5), 2, RoundingMode.HALF_UP));
						m.setWindSpeed(h.windSpeed() == null ? null : h.windSpeed().multiply(BigDecimal.valueOf(18)).divide(BigDecimal.valueOf(5), 2, RoundingMode.HALF_UP));
						m.setCreated(new Timestamp((h.dt()/* + data.timezoneOffset()*/) * 1000));
						measurements.add(m);
					});
					data.hourly().forEach(h -> {
						Measurements m = new Measurements();
						m.setAmbientTemp(h.temp());
						m.setHumidity(h.humidity());
						m.setRainfall(h.precipitation());
						m.setPressure(h.pressure());
						m.setHumidity(h.humidity());
						m.setWindGust(h.windGust() == null ? null : h.windGust().multiply(BigDecimal.valueOf(18)).divide(BigDecimal.valueOf(5), 2, RoundingMode.HALF_UP));
						m.setWindSpeed(h.windSpeed() == null ? null : h.windSpeed().multiply(BigDecimal.valueOf(18)).divide(BigDecimal.valueOf(5), 2, RoundingMode.HALF_UP));
						m.setCreated(new Timestamp((h.dt()/* + data.timezoneOffset()*/) * 1000));
						measurements.add(m);
					});

					FORECAST = Collections.unmodifiableList(measurements);
				}
				else
				{
					Logger.getLogger("").warning(response.message());
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
