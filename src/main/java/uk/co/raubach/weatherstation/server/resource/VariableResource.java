package uk.co.raubach.weatherstation.server.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.jooq.DSLContext;
import uk.co.raubach.weatherstation.server.database.Database;

import java.sql.*;

import static uk.co.raubach.weatherstation.server.database.codegen.tables.Measurements.MEASUREMENTS;

@Path("variable")
public class VariableResource
{
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVariables()
			throws SQLException
	{
		try (Connection conn = Database.getDirectConnection())
		{
			DSLContext context = Database.getContext(conn);
			return Response.ok(MEASUREMENTS.fieldStream().filter(f -> f.getDataType(context.configuration()).getSQLType() == Types.DECIMAL).map(f -> f.getName()).toList()).build();
		}
	}
}
