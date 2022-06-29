package uk.co.raubach.weatherstation.server.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import uk.co.raubach.weatherstation.server.database.Database;

import java.sql.*;

import static uk.co.raubach.weatherstation.server.database.codegen.tables.Measurements.*;

@Path("/latest")
public class LastUpdateResource extends ContextResource
{
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLastUpdate()
		throws SQLException
	{
		try (Connection conn = Database.getDirectConnection();
			 DSLContext context = Database.getContext(conn))
		{
			return Response.ok(context.select(DSL.max(MEASUREMENTS.CREATED)).from(MEASUREMENTS).fetchInto(Timestamp.class)).build();
		}
	}
}
