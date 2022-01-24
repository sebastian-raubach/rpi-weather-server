package uk.co.raubach.weatherstation.server.resource;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.restlet.data.Status;
import org.restlet.resource.*;
import uk.co.raubach.weatherstation.server.database.Database;

import java.sql.*;
import java.util.List;

import static uk.co.raubach.weatherstation.server.database.codegen.tables.Measurements.*;

public class YearResource extends ServerResource
{
	@Get
	public List<Integer> getYears()
	{
		try (Connection conn = Database.getDirectConnection();
			 DSLContext context = Database.getContext(conn))
		{
			Field<Integer> year = DSL.year(MEASUREMENTS.CREATED).as("year");
			return context.selectDistinct(year).from(MEASUREMENTS).orderBy(year).fetchInto(Integer.class);
		}
		catch (SQLException e)
		{
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
		}
	}
}
