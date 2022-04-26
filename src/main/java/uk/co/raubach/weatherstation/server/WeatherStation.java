package uk.co.raubach.weatherstation.server;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * @author Sebastian Raubach
 */
@ApplicationPath("/api/")
public class WeatherStation extends ResourceConfig
{
	public WeatherStation()
	{
		packages("uk.co.raubach.weatherstation.server");
	}
}
