package uk.co.raubach.weatherstation.server.util;

import org.jooq.*;
import org.jooq.impl.*;
import uk.co.raubach.weatherstation.server.database.Database;

import java.sql.*;
import java.util.logging.Logger;

import static uk.co.raubach.weatherstation.server.database.codegen.tables.Aggregated.AGGREGATED;
import static uk.co.raubach.weatherstation.server.database.codegen.tables.AggregatedYearMonth.AGGREGATED_YEAR_MONTH;
import static uk.co.raubach.weatherstation.server.database.codegen.tables.Measurements.MEASUREMENTS;

public class AggregatedCalculatorThread implements Runnable
{
	@Override
	public void run()
	{
		double windFactor = 1d;
		double tempOffset = 0d;
		try
		{
			windFactor = Double.parseDouble(PropertyWatcher.get("wind.strength.multiplier"));
		}
		catch (Exception e)
		{
		}
		try
		{
			tempOffset = Double.parseDouble(PropertyWatcher.get("temperature.offset"));
		}
		catch (Exception e)
		{
		}

		try (Connection conn = Database.getDirectConnection())
		{
			DSLContext context = Database.getContext(conn);
			context.deleteFrom(AGGREGATED).execute();

			Field<String> date = dateFormat(MEASUREMENTS.CREATED, "%Y-%m-%d").as("date");

			context.insertInto(AGGREGATED)
				   .select(context.select(
										  DSL.min(MEASUREMENTS.AMBIENT_TEMP).add(tempOffset).as("min_ambient_temp"),
										  DSL.min(MEASUREMENTS.GROUND_TEMP).add(tempOffset).as("min_ground_temp"),
										  DSL.min(MEASUREMENTS.PI_TEMP).as("min_pi_temp"),
										  DSL.min(MEASUREMENTS.LUX).as("min_lux"),
										  DSL.min(MEASUREMENTS.LOFT_HUMIDITY).as("min_loft_humidity"),
										  DSL.min(MEASUREMENTS.LOFT_TEMP).as("min_loft_temp"),
										  DSL.min(MEASUREMENTS.PRESSURE).as("min_pressure"),
										  DSL.min(MEASUREMENTS.HUMIDITY).as("min_humidity"),
										  DSL.min(MEASUREMENTS.WIND_AVERAGE).as("min_wind_average"),
										  DSL.min(MEASUREMENTS.WIND_SPEED).times(windFactor).as("min_wind_speed"),
										  DSL.min(MEASUREMENTS.WIND_GUST).times(windFactor).as("min_wind_gust"),
										  DSL.max(MEASUREMENTS.AMBIENT_TEMP).add(tempOffset).as("max_ambient_temp"),
										  DSL.max(MEASUREMENTS.GROUND_TEMP).add(tempOffset).as("max_ground_temp"),
										  DSL.max(MEASUREMENTS.PI_TEMP).as("max_pi_temp"),
										  DSL.max(MEASUREMENTS.LUX).as("max_lux"),
										  DSL.max(MEASUREMENTS.LOFT_HUMIDITY).as("max_loft_humidity"),
										  DSL.max(MEASUREMENTS.LOFT_TEMP).as("max_loft_temp"),
										  DSL.max(MEASUREMENTS.PRESSURE).as("max_pressure"),
										  DSL.max(MEASUREMENTS.HUMIDITY).as("max_humidity"),
										  DSL.max(MEASUREMENTS.WIND_AVERAGE).as("max_wind_average"),
										  DSL.max(MEASUREMENTS.WIND_SPEED).times(windFactor).as("max_wind_speed"),
										  DSL.max(MEASUREMENTS.WIND_GUST).times(windFactor).as("max_wind_gust"),
										  DSL.avg(MEASUREMENTS.AMBIENT_TEMP.add(tempOffset)).as("avg_ambient_temp"),
										  DSL.avg(MEASUREMENTS.GROUND_TEMP.add(tempOffset)).as("avg_ground_temp"),
										  DSL.avg(MEASUREMENTS.PI_TEMP).as("avg_pi_temp"),
										  DSL.avg(MEASUREMENTS.LUX).as("avg_lux"),
										  DSL.avg(MEASUREMENTS.LOFT_HUMIDITY).as("avg_loft_humidity"),
										  DSL.avg(MEASUREMENTS.LOFT_TEMP).as("avg_loft_temp"),
										  DSL.avg(MEASUREMENTS.PRESSURE).as("avg_pressure"),
										  DSL.avg(MEASUREMENTS.HUMIDITY).as("avg_humidity"),
										  DSL.avg(MEASUREMENTS.WIND_AVERAGE).as("avg_wind_average"),
										  DSL.avg(MEASUREMENTS.WIND_SPEED.times(windFactor)).as("avg_wind_speed"),
										  DSL.avg(MEASUREMENTS.WIND_GUST.times(windFactor)).as("avg_wind_gust"),
										  DSL.stddevPop(MEASUREMENTS.AMBIENT_TEMP.add(tempOffset)).as("std_ambient_temp"),
										  DSL.stddevPop(MEASUREMENTS.GROUND_TEMP.add(tempOffset)).as("std_ground_temp"),
										  DSL.stddevPop(MEASUREMENTS.PI_TEMP).as("std_pi_temp"),
										  DSL.stddevPop(MEASUREMENTS.LUX).as("std_lux"),
										  DSL.stddevPop(MEASUREMENTS.LOFT_HUMIDITY).as("std_loft_humidity"),
										  DSL.stddevPop(MEASUREMENTS.LOFT_TEMP).as("std_loft_temp"),
										  DSL.stddevPop(MEASUREMENTS.PRESSURE).as("std_pressure"),
										  DSL.stddevPop(MEASUREMENTS.HUMIDITY).as("std_humidity"),
										  DSL.stddevPop(MEASUREMENTS.WIND_AVERAGE).as("std_wind_average"),
										  DSL.stddevPop(MEASUREMENTS.WIND_SPEED.times(windFactor)).as("std_wind_speed"),
										  DSL.stddevPop(MEASUREMENTS.WIND_GUST.times(windFactor)).as("std_wind_gust"),
										  DSL.sum(MEASUREMENTS.RAINFALL).as("sum_rainfall"),
										  date
								  ).from(MEASUREMENTS)
								  .groupBy(date))
				   .execute();

			context.deleteFrom(AGGREGATED_YEAR_MONTH).execute();

			Field<Integer> year = DSL.year(MEASUREMENTS.CREATED).as("year");
			Field<Integer> month = DSL.month(MEASUREMENTS.CREATED).as("month");
			context.insertInto(AGGREGATED_YEAR_MONTH)
				   .select(context.select(
										  DSL.avg(MEASUREMENTS.AMBIENT_TEMP.add(tempOffset)).as("avg_ambient_temp"),
										  DSL.avg(MEASUREMENTS.GROUND_TEMP.add(tempOffset)).as("avg_ground_temp"),
										  DSL.avg(MEASUREMENTS.LOFT_HUMIDITY).as("avg_loft_humidity"),
										  DSL.avg(MEASUREMENTS.LOFT_TEMP).as("avg_loft_temp"),
										  DSL.avg(MEASUREMENTS.LUX).as("avg_lux"),
										  DSL.avg(MEASUREMENTS.PRESSURE).as("avg_pressure"),
										  DSL.avg(MEASUREMENTS.HUMIDITY).as("avg_humidity"),
										  DSL.avg(MEASUREMENTS.WIND_SPEED.times(windFactor)).as("avg_wind_speed"),
										  DSL.avg(MEASUREMENTS.WIND_GUST.times(windFactor)).as("avg_wind_gust"),
										  DSL.sum(MEASUREMENTS.RAINFALL).as("sum_rainfall"),
										  year,
										  month
								  ).from(MEASUREMENTS)
								  .groupBy(year, month))
				   .execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			Logger.getLogger("").severe(e.getMessage());
		}
	}

	private static Field<String> dateFormat(Field<Timestamp> field, String format)
	{
		return DSL.field("date_format({0}, {1})", SQLDataType.VARCHAR, field, DSL.inline(format));
	}
}
