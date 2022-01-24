package uk.co.raubach.weatherstation.server.resource;

import org.jooq.DSLContext;
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
			return context.selectDistinct(DSL.year(MEASUREMENTS.CREATED)).from(MEASUREMENTS).orderBy(MEASUREMENTS.CREATED).fetchInto(Integer.class);
		}
		catch (SQLException e)
		{
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
		}
	}
}
