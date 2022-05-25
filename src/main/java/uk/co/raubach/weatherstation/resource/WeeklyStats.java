package uk.co.raubach.weatherstation.resource;

import java.math.BigDecimal;
import java.util.Date;

public class WeeklyStats
{
	private Day highestTemp;
	private Day lowestTemp;
	private Day mostRain;
	private Day mostWind;
	private BigDecimal avgTemp;
	private BigDecimal totalRain;

	public Day getHighestTemp()
	{
		return highestTemp;
	}

	public WeeklyStats setHighestTemp(Day highestTemp)
	{
		this.highestTemp = highestTemp;
		return this;
	}

	public Day getLowestTemp()
	{
		return lowestTemp;
	}

	public WeeklyStats setLowestTemp(Day lowestTemp)
	{
		this.lowestTemp = lowestTemp;
		return this;
	}

	public Day getMostRain()
	{
		return mostRain;
	}

	public WeeklyStats setMostRain(Day mostRain)
	{
		this.mostRain = mostRain;
		return this;
	}

	public Day getMostWind()
	{
		return mostWind;
	}

	public WeeklyStats setMostWind(Day mostWind)
	{
		this.mostWind = mostWind;
		return this;
	}

	public BigDecimal getAvgTemp()
	{
		return avgTemp;
	}

	public WeeklyStats setAvgTemp(BigDecimal avgTemp)
	{
		this.avgTemp = avgTemp;
		return this;
	}

	public BigDecimal getTotalRain()
	{
		return totalRain;
	}

	public WeeklyStats setTotalRain(BigDecimal totalRain)
	{
		this.totalRain = totalRain;
		return this;
	}

	@Override
	public String toString()
	{
		return "WeeklyStats{" +
			"highestTemp=" + highestTemp +
			", lowestTemp=" + lowestTemp +
			", mostRain=" + mostRain +
			", mostWind=" + mostWind +
			", avgTemp=" + avgTemp +
			", totalRain=" + totalRain +
			'}';
	}

	public static class Day
	{
		private Date       date;
		private BigDecimal value;

		public Date getDate()
		{
			return date;
		}

		public Day setDate(Date date)
		{
			this.date = date;
			return this;
		}

		public BigDecimal getValue()
		{
			return value;
		}

		public Day setValue(BigDecimal value)
		{
			this.value = value;
			return this;
		}

		@Override
		public String toString()
		{
			return "Day{" +
				"date=" + date +
				", value=" + value +
				'}';
		}
	}
}
