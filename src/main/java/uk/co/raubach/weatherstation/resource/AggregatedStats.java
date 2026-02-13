package uk.co.raubach.weatherstation.resource;

import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class AggregatedStats
{
	private Day highestTemp;
	private Day lowestTemp;
	private Day mostRain;
	private Day mostWind;
	private BigDecimal avgTemp;
	private BigDecimal totalRain;
	private BigDecimal avgLux;
	private BigDecimal avgHumidity;
	private BigDecimal avgLoftHumidity;
	private BigDecimal avgLoftTemperature;

	@Getter
	@Setter
	@NoArgsConstructor
	@ToString
	@Accessors(chain = true)
	public static class Day
	{
		private Date       date;
		private BigDecimal value;
	}
}
