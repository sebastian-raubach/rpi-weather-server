package uk.co.raubach.weatherstation.resource;

import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class AggregatedStats
{
	private Day        highestTemp;
	private Day        lowestTemp;
	private Day        mostRain;
	private Day        mostIntenseRain;
	private Day        mostWind;
	private BigDecimal minTemp;
	private BigDecimal avgTemp;
	private BigDecimal maxTemp;
	private BigDecimal totalRain;
	private BigDecimal avgLux;
	private BigDecimal avgHumidity;
	private BigDecimal avgLoftHumidity;
	private BigDecimal avgLoftTemperature;
}
