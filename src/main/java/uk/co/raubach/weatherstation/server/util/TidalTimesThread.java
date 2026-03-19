package uk.co.raubach.weatherstation.server.util;

import com.google.gson.*;
import okhttp3.*;
import uk.co.raubach.weatherstation.resource.TidalInfo;
import uk.co.raubach.weatherstation.server.util.stormglass.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class TidalTimesThread implements Runnable
{
	public static TidalInfo INFO = new TidalInfo();

	@Override
	public void run()
	{
		this.getTidalTimes();
	}

	private void getTidalTimes()
	{
		OkHttpClient client = new OkHttpClient();

		HttpUrl.Builder builder = HttpUrl.parse("https://api.stormglass.io/v2/tide/sea-level/point").newBuilder()
										 .addQueryParameter("lat", PropertyWatcher.get("latitude"))
										 .addQueryParameter("lng", PropertyWatcher.get("longitude"));

		HttpUrl builtUrl = builder.build();

		Request request = new Request.Builder()
				.header("Authorization", PropertyWatcher.get("stormglass.api.key"))
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

					StormglassLevel levels = gson.fromJson(response.body().string(), StormglassLevel.class);
					List<LevelData> data = new ArrayList<>(levels.getData());
					data.sort((a, b) -> (int) Math.signum(a.getTime().getTime() - b.getTime().getTime()));
					INFO.setLevels(data);
				}
				else
				{
					Logger.getLogger("").warning(response.message());
				}
			}
		}
		catch (IOException e)
		{
			Logger.getLogger("").severe(e.getMessage());
			e.printStackTrace();
		}

		builder = HttpUrl.parse("https://api.stormglass.io/v2/tide/extremes/point").newBuilder()
						 .addQueryParameter("lat", PropertyWatcher.get("latitude"))
						 .addQueryParameter("lng", PropertyWatcher.get("longitude"));

		builtUrl = builder.build();

		request = new Request.Builder()
				.header("Authorization", PropertyWatcher.get("stormglass.api.key"))
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

					StormglassExtreme extremes = gson.fromJson(response.body().string(), StormglassExtreme.class);
					List<ExtremeData> data = new ArrayList<>(extremes.getData());
					data.sort((a, b) -> (int) Math.signum(a.getTime().getTime() - b.getTime().getTime()));
					INFO.setExtremes(data);
				}
				else
				{
					Logger.getLogger("").warning(response.message());
				}
			}
		}
		catch (IOException e)
		{
			Logger.getLogger("").severe(e.getMessage());
			e.printStackTrace();
		}
	}

}
