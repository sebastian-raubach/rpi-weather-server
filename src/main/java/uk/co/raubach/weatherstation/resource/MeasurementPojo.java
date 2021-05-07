package uk.co.raubach.weatherstation.resource;

import java.math.BigDecimal;

public class MeasurementPojo
{
	private BigDecimal ambientTemp;
	private BigDecimal groundTemp;
	private BigDecimal pressure;
	private BigDecimal humidity;
	private BigDecimal windAverage;
	private BigDecimal windSpeed;
	private BigDecimal windGust;
	private BigDecimal rainfall;
	private BigDecimal piTemp;
	private String     created;

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

	public BigDecimal getPiTemp()
	{
		return piTemp;
	}

	public void setPiTemp(BigDecimal piTemp)
	{
		this.piTemp = piTemp;
	}

	public String getCreated()
	{
		return created;
	}

	public void setCreated(String created)
	{
		this.created = created;
	}
}
