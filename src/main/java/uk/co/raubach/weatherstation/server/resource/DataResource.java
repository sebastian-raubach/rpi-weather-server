package uk.co.raubach.weatherstation.server.resource;

import org.jooq.DSLContext;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.data.Status;
import org.restlet.resource.*;
import uk.co.raubach.weatherstation.server.database.Database;
import uk.co.raubach.weatherstation.server.database.codegen.tables.pojos.Measurements;
import uk.co.raubach.weatherstation.server.util.PropertyWatcher;

import java.sql.*;
import java.util.*;

import static uk.co.raubach.weatherstation.server.database.codegen.tables.Measurements.*;

public class DataResource extends ServerResource
{
	public static final String PARAM_UUID = "uuid";

	private String uuid;

	@Override
	protected void doInit()
		throws ResourceException
	{
		super.doInit();

		try
		{
			this.uuid = getQueryValue(PARAM_UUID);
		}
		catch (Exception e)
		{
		}
	}

	@Post
	public void postData(Measurements[] measurements)
	{
		if (!Objects.equals(this.uuid, PropertyWatcher.get("client.uuid")))
		{
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED);
		}
		else if (measurements == null)
		{
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
		else
		{
			try (Connection conn = Database.getDirectConnection();
				 DSLContext context = Database.getContext(conn))
			{
				Arrays.stream(measurements)
					  .filter(m -> m.getCreated() != null)
					  .map(m -> context.newRecord(MEASUREMENTS, m))
					  .forEach(UpdatableRecordImpl::store);
			}
			catch (SQLException e)
			{
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
			}
		}
	}
}
