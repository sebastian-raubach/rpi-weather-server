package uk.co.raubach.weatherstation.server.util;

import com.google.gson.*;

import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.Date;

public class GsonUtil
{
	public static final String PATTERN_TIMESTAMP = "yyyy-MM-dd'T'HH:mm:ss.SSSXX";
	public static final String PATTERN_DATE = "yyyy-MM-dd";

	private static Gson              gson;
	private static DateTimeFormatter sdf;
	private static DateTimeFormatter  sdfDate;

	public static synchronized Gson getInstance()
	{
		if (gson == null)
		{
			gson = getGsonBuilderInstance().create();
		}
		return gson;
	}

	public static synchronized DateTimeFormatter getSDFInstance()
	{
		if (sdf == null)
		{
			sdf = DateTimeFormatter.ofPattern(PATTERN_TIMESTAMP).withZone(ZoneId.systemDefault());
		}
		return sdf;
	}

	public static synchronized DateTimeFormatter getSDFInstanceDate()
	{
		if (sdfDate == null)
		{
			sdfDate = DateTimeFormatter.ofPattern(PATTERN_DATE).withZone(ZoneId.systemDefault());
		}
		return sdfDate;
	}

	private static GsonBuilder getGsonBuilderInstance()
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, type, arg2) -> {
			try
			{
				return new Date(OffsetDateTime.parse(json.getAsString(), getSDFInstance()).toInstant().toEpochMilli());
			}
			catch (DateTimeParseException e)
			{
				return null;
			}
		});
		gsonBuilder.registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(getSDFInstance()
			.format(src.toInstant())));
		gsonBuilder.registerTypeAdapter(java.sql.Date.class, (JsonDeserializer<java.sql.Date>) (json, type, arg2) -> {
			try
			{
				return new java.sql.Date(OffsetDateTime.parse(json.getAsString(), getSDFInstanceDate()).toInstant().toEpochMilli());
			}
			catch (DateTimeParseException e)
			{
				return null;
			}
		});
		gsonBuilder.registerTypeAdapter(java.sql.Date.class, (JsonSerializer<java.sql.Date>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(getSDFInstanceDate()
			.format(new Date(src.getTime()).toInstant())));
		return gsonBuilder;
	}
}