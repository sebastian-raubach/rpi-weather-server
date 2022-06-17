package uk.co.raubach.weatherstation.server.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.jooq.*;
import org.jooq.impl.DSL;
import uk.co.raubach.weatherstation.server.database.Database;

import java.sql.*;

import static uk.co.raubach.weatherstation.server.database.codegen.tables.Measurements.*;

@Path("stats/years")
public class YearResource extends ContextResource
{
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getYears()
		throws IndexOutOfBoundsException, SQLException
	{
		try (Connection conn = Database.getDirectConnection();
			 DSLContext context = Database.getContext(conn))
		{
			Field<Integer> year = DSL.year(MEASUREMENTS.CREATED).as("year");
			return Response.ok(context.selectDistinct(year).from(MEASUREMENTS).orderBy(year).fetchInto(Integer.class)).build();
		}
	}
}
