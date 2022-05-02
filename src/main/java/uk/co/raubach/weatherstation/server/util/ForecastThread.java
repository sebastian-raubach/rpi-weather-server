package uk.co.raubach.weatherstation.server.util;

import com.google.gson.*;
import okhttp3.*;
import uk.co.raubach.weatherstation.resource.*;
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
//		this.getOpenWeathermapData();
		this.getMetNoData();
	}

	private void getMetNoData()
	{
		OkHttpClient client = new OkHttpClient();

		HttpUrl.Builder builder = HttpUrl.parse("https://api.met.no/weatherapi/locationforecast/2.0/compact").newBuilder()
										 .addQueryParameter("lat", PropertyWatcher.get("latitude"))
										 .addQueryParameter("lon", PropertyWatcher.get("longitude"));

		HttpUrl builtUrl = builder.build();

		Request request = new Request.Builder()
			.header("User-Agent", "rpi-weather-station/1.0 sebastian@raubach.co.uk")
			.get()
			.url(builtUrl)
			.build();

		try
		{
			try (Response response = client.newCall(request).execute())
			{
				if (response.isSuccessful())
				{
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

					MetNoForecast forecast = gson.fromJson(response.body().string(), MetNoForecast.class);

					List<Measurements> measurements = new ArrayList<>();

					if (forecast != null && forecast.properties() != null)
					{
						forecast.properties().timeseries().forEach(t -> {
							MetNoForecast.Data data = t.data();

							MetNoForecast.Details i = data.instant().details();
							if (data.nextOneHour() != null)
							{
								i.precipitation(data.nextOneHour().details().precipitation());
							}

							Measurements m = new Measurements();
							m.setAmbientTemp(i.ambientTemp());
							m.setHumidity(i.humidity());
							m.setRainfall(i.precipitation());
							m.setPressure(i.pressure());
							m.setHumidity(i.humidity());
							m.setWindSpeed(i.windSpeed() == null ? null : i.windSpeed().multiply(BigDecimal.valueOf(18)).divide(BigDecimal.valueOf(5), 2, RoundingMode.HALF_UP));
							m.setCreated(new Timestamp(t.time().getTime()));
							measurements.add(m);
						});
					}

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

	private void getOpenWeathermapData()
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
