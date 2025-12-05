package uk.co.raubach.weatherstation.resource;

import java.math.BigDecimal;
import java.util.Date;

public class DailyStats
{
	private Date      date;
	private TypeStats avg;
	private TypeStats min;
	private TypeStats max;
	private TypeStats stdv;

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public TypeStats getAvg()
	{
		return avg;
	}

	public void setAvg(TypeStats avg)
	{
		this.avg = avg;
	}

	public TypeStats getMin()
	{
		return min;
	}

	public void setMin(TypeStats min)
	{
		this.min = min;
	}

	public TypeStats getMax()
	{
		return max;
	}

	public void setMax(TypeStats max)
	{
		this.max = max;
	}

	public TypeStats getStdv()
	{
		return stdv;
	}

	public void setStdv(TypeStats stdv)
	{
		this.stdv = stdv;
	}

	public static class TypeStats
	{
		private BigDecimal ambientTemp;
		private BigDecimal groundTemp;
		private BigDecimal loftTemp;
		private BigDecimal piTemp;
		private BigDecimal pressure;
		private BigDecimal humidity;
		private BigDecimal loftHumidity;
		private BigDecimal windAverage;
		private BigDecimal windSpeed;
		private BigDecimal windGust;
		private BigDecimal rainfall;
		private BigDecimal lux;

		public TypeStats()
		{
		}

		public BigDecimal getAmbientTemp()
		{
			return ambientTemp;
		}

		public void setAmbientTemp(BigDecimal ambientTemp)
		{
			this.ambientTemp = ambientTemp;
		}

		public BigDecimal getGroundTemp()
		{
			return groundTemp;
		}

		public void setGroundTemp(BigDecimal groundTemp)
		{
			this.groundTemp = groundTemp;
		}

		public BigDecimal getPiTemp()
		{
			return piTemp;
		}

		public TypeStats setPiTemp(BigDecimal piTemp)
		{
			this.piTemp = piTemp;
			return this;
		}

		public BigDecimal getPressure()
		{
			return pressure;
		}

		public void setPressure(BigDecimal pressure)
		{
			this.pressure = pressure;
		}

		public BigDecimal getHumidity()
		{
			return humidity;
		}

		public void setHumidity(BigDecimal humidity)
		{
			this.humidity = humidity;
		}

		public BigDecimal getWindAverage()
		{
			return windAverage;
		}

		public void setWindAverage(BigDecimal windAverage)
		{
			this.windAverage = windAverage;
		}

		public BigDecimal getWindSpeed()
		{
			return windSpeed;
		}

		public void setWindSpeed(BigDecimal windSpeed)
		{
			this.windSpeed = windSpeed;
		}

		public BigDecimal getWindGust()
		{
			return windGust;
		}

		public void setWindGust(BigDecimal windGust)
		{
			this.windGust = windGust;
		}

		public BigDecimal getRainfall()
		{
			return rainfall;
		}

		public void setRainfall(BigDecimal rainfall)
		{
			this.rainfall = rainfall;
		}

		public BigDecimal getLux()
		{
			return lux;
		}

		public TypeStats setLux(BigDecimal lux)
		{
			this.lux = lux;
			return this;
		}

		public BigDecimal getLoftTemp()
		{
			return loftTemp;
		}

		public TypeStats setLoftTemp(BigDecimal loftTemp)
		{
			this.loftTemp = loftTemp;
			return this;
		}

		public BigDecimal getLoftHumidity()
		{
			return loftHumidity;
		}

		public TypeStats setLoftHumidity(BigDecimal loftHumidity)
		{
			this.loftHumidity = loftHumidity;
			return this;
		}
	}
}
