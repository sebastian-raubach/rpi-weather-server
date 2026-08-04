package uk.co.raubach.weatherstation.server.util.lux;

import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.*;
import java.util.*;

import static org.jooq.impl.DSL.falseCondition;
import static uk.co.raubach.weatherstation.server.database.codegen.tables.LuxClimatology.LUX_CLIMATOLOGY;
import static uk.co.raubach.weatherstation.server.database.codegen.tables.Measurements.MEASUREMENTS;

/**
 * Rebuilds the `lux_climatology` cache table (see schema.sql).
 * <p>
 * For every calendar day of the year, pools lux readings from a +/- window
 * of days around that date across all previous years, takes the 95th
 * percentile per 5-minute slot, smooths across the day's 288 slots, and
 * upserts the result.
 * <p>
 * Run this on a schedule (e.g. nightly) - the baseline changes slowly, so
 * it doesn't need to be recomputed on every request.
 * <p>
 * Config via env vars: DB_URL, DB_USER, DB_PASSWORD.
 * Adjust table/column names below to match your schema.
 */
public class ClimatologyBuilder
{
	private static final int    WINDOW_DAYS   = 7;
	private static final double PERCENTILE    = 95.0;
	private static final int    SLOT_SECONDS  = 5 * 60;
	private static final int    SLOTS_PER_DAY = 24 * 3600 / SLOT_SECONDS;

	public static Map<Integer, List<Double>> fetchWindowReadings(
			DSLContext ctx, int month, int day, int excludeYear)
	{
		Set<DayWindow.MonthDay> pairs = DayWindow.forDate(month, day, WINDOW_DAYS);

		Condition dateCondition = falseCondition();
		for (DayWindow.MonthDay p : pairs)
		{
			dateCondition = dateCondition.or(DSL.field(DSL.name("created_month"), Integer.class).eq(p.month()).and(DSL.field(DSL.name("created_day"), Integer.class).eq(p.day())));
		}

		Result<Record2<Timestamp, Double>> rows = ctx
				.select(MEASUREMENTS.CREATED, MEASUREMENTS.LUX.cast(Double.class))
				.from(MEASUREMENTS)
				.where(dateCondition)
				.and(DSL.year(MEASUREMENTS.CREATED).ne(excludeYear))
				.and(MEASUREMENTS.LUX.isNotNull())
				.fetch();

		Map<Integer, List<Double>> bySlot = new HashMap<>();
		for (Record2<Timestamp, Double> row : rows)
		{
			int secondsOfDay = row.value1().toLocalDateTime().toLocalTime().toSecondOfDay();
			int slot = (secondsOfDay / SLOT_SECONDS) * SLOT_SECONDS;
			bySlot.computeIfAbsent(slot, k -> new ArrayList<>()).add(row.value2());
		}
		return bySlot;
	}

	public static void upsertClimatology(
			DSLContext ctx, int month, int day, Map<Integer, List<Double>> valuesBySlot)
	{

		double[] raw = new double[SLOTS_PER_DAY];
		int[] counts = new int[SLOTS_PER_DAY];
		for (int i = 0; i < SLOTS_PER_DAY; i++)
		{
			List<Double> values = valuesBySlot.get(i * SLOT_SECONDS);
			if (values != null && !values.isEmpty())
			{
				raw[i] = SlotStats.percentile(values, PERCENTILE);
				counts[i] = values.size();
			}
			else
			{
				raw[i] = Double.NaN;
				counts[i] = 0;
			}
		}
		SlotStats.fillGaps(raw);
		double[] smoothed = SlotStats.smoothCircular(raw);

		InsertValuesStep6<?, Integer, Integer, Time, Double, Double, Integer> insert = ctx.insertInto(
				LUX_CLIMATOLOGY, LUX_CLIMATOLOGY.MONTH, LUX_CLIMATOLOGY.DAY, LUX_CLIMATOLOGY.SLOT_TIME, LUX_CLIMATOLOGY.P95_LUX, LUX_CLIMATOLOGY.P95_LUX_SMOOTH, LUX_CLIMATOLOGY.SAMPLE_COUNT);

		for (int i = 0; i < SLOTS_PER_DAY; i++)
		{
			int secondsOfDay = i * SLOT_SECONDS;
			insert = insert.values(month, day, new Time(secondsOfDay * 1000), raw[i], smoothed[i], counts[i]);
		}

		insert.onDuplicateKeyUpdate()
		      .set(LUX_CLIMATOLOGY.P95_LUX, DSL.field("VALUES(p95_lux)", Double.class))
		      .set(LUX_CLIMATOLOGY.P95_LUX_SMOOTH, DSL.field("VALUES(p95_lux_smooth)", Double.class))
		      .set(LUX_CLIMATOLOGY.SAMPLE_COUNT, DSL.field("VALUES(sample_count)", Integer.class))
		      .execute();
	}
}
