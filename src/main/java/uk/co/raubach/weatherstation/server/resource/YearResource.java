package uk.co.raubach.weatherstation.server.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.*;
import org.jooq.*;
import org.jooq.impl.DSL;
import uk.co.raubach.weatherstation.server.database.Database;

import java.sql.*;
import java.util.List;

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
		try (Connection conn = Database.getDirectConnection())
		{
			DSLContext context = Database.getContext(conn);
			List<Integer> years = context.selectDistinct(DSL.year(MEASUREMENTS.CREATED)).from(MEASUREMENTS).fetchInto(Integer.class);
			years.sort(Integer::compareTo);

			return Response.ok(years).build();
		}
	}
}
