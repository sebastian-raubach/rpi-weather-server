package uk.co.raubach.weatherstation.resource;

import uk.co.raubach.weatherstation.server.database.codegen.tables.pojos.Measurements;

import java.math.BigDecimal;

public class ExtendedMeasurement extends Measurements
{
	private BigDecimal heatIndex;

	public BigDecimal getHeatIndex()
	{
		return heatIndex;
	}

	public ExtendedMeasurement setHeatIndex(BigDecimal heatIndex)
	{
		this.heatIndex = heatIndex;
		return this;
	}
}
