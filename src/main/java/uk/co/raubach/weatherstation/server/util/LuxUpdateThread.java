package uk.co.raubach.weatherstation.server.util;

import org.jooq.DSLContext;
import uk.co.raubach.weatherstation.server.database.Database;
import uk.co.raubach.weatherstation.server.util.lux.ClimatologyBuilder;

import java.sql.Connection;
import java.time.*;
import java.util.*;
import java.util.logging.Logger;

public class LuxUpdateThread implements Runnable
{
	@Override
	public void run()
	{
		try (Connection conn = Database.getDirectConnection())
		{
			DSLContext ctx = Database.getContext(conn);

			int currentYear = Year.now().getValue();

			for (int month = 1; month <= 12; month++)
			{
				int daysInMonth = LocalDate.of(2020, month, 1).lengthOfMonth(); // 2020 = leap year scratch calendar
				for (int day = 1; day <= daysInMonth; day++)
				{
					Map<Integer, List<Double>> valuesBySlot = ClimatologyBuilder.fetchWindowReadings(ctx, month, day, currentYear);
					if (valuesBySlot.isEmpty())
						continue;
					ClimatologyBuilder.upsertClimatology(ctx, month, day, valuesBySlot);
				}
			}
		}
		catch (Exception e)
		{
			Logger.getLogger("").severe(e.getMessage());
		}
	}
}
