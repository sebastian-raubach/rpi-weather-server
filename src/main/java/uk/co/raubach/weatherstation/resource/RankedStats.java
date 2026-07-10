package uk.co.raubach.weatherstation.resource;

import lombok.*;
import lombok.experimental.Accessors;
import uk.co.raubach.weatherstation.server.database.codegen.tables.pojos.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class RankedStats
{
	private List<Day> highestTemp;
	private List<Day> lowestTemp;
	private List<Day> highestRain;
	private List<Day> highestWind;
	private ViewPeriods longestDryPeriod;
	private ViewPeriods longestWetPeriod;
}
