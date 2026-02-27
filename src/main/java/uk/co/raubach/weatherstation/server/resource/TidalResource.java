package uk.co.raubach.weatherstation.server.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import uk.co.raubach.weatherstation.server.util.TidalTimesThread;

import java.sql.SQLException;

@Path("tide")
public class TidalResource extends ContextResource
{
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLastUpdate()
			throws SQLException
	{
		return Response.ok(TidalTimesThread.INFO).build();
	}
}
