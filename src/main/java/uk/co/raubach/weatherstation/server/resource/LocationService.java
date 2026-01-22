package uk.co.raubach.weatherstation.server.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import uk.co.raubach.weatherstation.resource.Location;
import uk.co.raubach.weatherstation.server.util.PropertyWatcher;

@Path("location")
public class LocationService
{
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocation()
	{
		try
		{
			double lat = Double.parseDouble(PropertyWatcher.get("latitude"));
			double lng = Double.parseDouble(PropertyWatcher.get("longitude"));

			return Response.ok(new Location(lat, lng)).build();
		}
		catch (Exception e)
		{
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
}
