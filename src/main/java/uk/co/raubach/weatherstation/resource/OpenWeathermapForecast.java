package uk.co.raubach.weatherstation.resource;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(fluent = true)
public class OpenWeathermapForecast
{
	private double              lat;
	private double              lon;
	@SerializedName("timezone_offset")
	private int                 timezoneOffset;
	private OWMeasurement       current;
	private List<OWMeasurement> minutely;
	private List<OWMeasurement> hourly;

	@Getter
	@Setter
	@NoArgsConstructor
	@ToString
	@Accessors(fluent = true)
	public static class OWMeasurement
	{
		private long       dt;
		private BigDecimal temp;
		private BigDecimal pressure;
		private BigDecimal humidity;
		@SerializedName("wind_speed")
		private BigDecimal windSpeed;
		@SerializedName("wind_gust")
		private BigDecimal windGust;
		private BigDecimal precipitation;
	}
}
