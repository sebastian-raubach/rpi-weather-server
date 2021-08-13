package uk.co.raubach.weatherstation.resource;

import org.jooq.Record;

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
		private Double ambientTemp;
		private Double groundTemp;
		private Double pressure;
		private Double humidity;
		private Double windAverage;
		private Double windSpeed;
		private Double windGust;
		private Double rainfall;

		public TypeStats()
		{
		}

		public TypeStats(Record r)
		{
			this.ambientTemp = r.get("ambient_temp", Double.class);
			this.groundTemp = r.get("ground_temp", Double.class);
			this.pressure = r.get("pressure", Double.class);
			this.humidity = r.get("humidity", Double.class);
			this.windAverage = r.get("wind_average", Double.class);
			this.windSpeed = r.get("wind_speed", Double.class);
			this.windGust = r.get("wind_gust", Double.class);
			this.rainfall = r.get("rainfall", Double.class);
		}

		public Double getAmbientTemp()
		{
			return ambientTemp;
		}

		public void setAmbientTemp(Double ambientTemp)
		{
			this.ambientTemp = ambientTemp;
		}

		public Double getGroundTemp()
		{
			return groundTemp;
		}

		public void setGroundTemp(Double groundTemp)
		{
			this.groundTemp = groundTemp;
		}

		public Double getPressure()
		{
			return pressure;
		}

		public void setPressure(Double pressure)
		{
			this.pressure = pressure;
		}

		public Double getHumidity()
		{
			return humidity;
		}

		public void setHumidity(Double humidity)
		{
			this.humidity = humidity;
		}

		public Double getWindAverage()
		{
			return windAverage;
		}

		public void setWindAverage(Double windAverage)
		{
			this.windAverage = windAverage;
		}

		public Double getWindSpeed()
		{
			return windSpeed;
		}

		public void setWindSpeed(Double windSpeed)
		{
			this.windSpeed = windSpeed;
		}

		public Double getWindGust()
		{
			return windGust;
		}

		public void setWindGust(Double windGust)
		{
			this.windGust = windGust;
		}

		public Double getRainfall()
		{
			return rainfall;
		}

		public void setRainfall(Double rainfall)
		{
			this.rainfall = rainfall;
		}
	}
}
