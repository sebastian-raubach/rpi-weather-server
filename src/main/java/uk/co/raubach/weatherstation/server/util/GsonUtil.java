package uk.co.raubach.weatherstation.server.util;

import com.google.gson.*;

import java.text.*;
import java.util.Date;

public class GsonUtil
{
	public static final String PATTERN_TIMESTAMP = "yyyy-MM-dd'T'HH:mm:ss.SSSXX";
	public static final String PATTERN_DATE = "yyyy-MM-dd";

	private static Gson             gson;
	private static SimpleDateFormat sdf;
	private static SimpleDateFormat sdfDate;

	public static synchronized Gson getInstance()
	{
		if (gson == null)
		{
			gson = getGsonBuilderInstance().create();
		}
		return gson;
	}

	public static synchronized SimpleDateFormat getSDFInstance()
	{
		if (sdf == null)
		{
			sdf = new SimpleDateFormat(PATTERN_TIMESTAMP);
		}
		return sdf;
	}

	public static synchronized SimpleDateFormat getSDFInstanceDate()
	{
		if (sdfDate == null)
		{
			sdfDate = new SimpleDateFormat(PATTERN_DATE);
		}
		return sdfDate;
	}

	private static GsonBuilder getGsonBuilderInstance()
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, type, arg2) -> {
			try
			{
				return getSDFInstance().parse(json.getAsString());
			}
			catch (ParseException e)
			{
				return null;
			}
		});
		gsonBuilder.registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(getSDFInstance()
			.format(src)));
		gsonBuilder.registerTypeAdapter(java.sql.Date.class, (JsonDeserializer<java.sql.Date>) (json, type, arg2) -> {
			try
			{
				return new java.sql.Date(getSDFInstanceDate().parse(json.getAsString()).getTime());
			}
			catch (ParseException e)
			{
				return null;
			}
		});
		gsonBuilder.registerTypeAdapter(java.sql.Date.class, (JsonSerializer<java.sql.Date>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(getSDFInstanceDate()
			.format(src)));
		return gsonBuilder;
	}
}