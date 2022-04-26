package uk.co.raubach.weatherstation.server.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
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
	public List<Integer> getYears()
		throws IndexOutOfBoundsException, SQLException
	{
		try (Connection conn = Database.getDirectConnection();
			 DSLContext context = Database.getContext(conn))
		{
			Field<Integer> year = DSL.year(MEASUREMENTS.CREATED).as("year");
			return context.selectDistinct(year).from(MEASUREMENTS).orderBy(year).fetchInto(Integer.class);
		}
	}
}
