package uk.co.raubach.weatherstation.resource;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(fluent = true)
public class MetNoForecast
{
	private Properties properties;

	@Getter
	@Setter
	@NoArgsConstructor
	@ToString
	@Accessors(fluent = true)
	public static class Properties
	{
		private List<Measurement> timeseries;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@ToString
	@Accessors(fluent = true)
	public static class Measurement
	{
		private Date time;
		private Data data;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@ToString
	@Accessors(fluent = true)
	public static class Data
	{
		private Instant instant;
		@SerializedName("next_1_hours")
		private NextHours nextOneHour;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@ToString
	@Accessors(fluent = true)
	public class NextHours {
		private Details details;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@ToString
	@Accessors(fluent = true)
	public static class Instant
	{
		private Details details;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@ToString
	@Accessors(fluent = true)
	public static class Details
	{
		@SerializedName("air_pressure_at_sea_level")
		private BigDecimal pressure;
		@SerializedName("air_temperature")
		private BigDecimal ambientTemp;
		@SerializedName("relative_humidity")
		private BigDecimal humidity;
		@SerializedName("wind_speed")
		private BigDecimal windSpeed;
		@SerializedName("precipitation_amount")
		private BigDecimal precipitation;
	}
}
