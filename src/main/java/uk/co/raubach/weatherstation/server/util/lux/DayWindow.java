package uk.co.raubach.weatherstation.server.util.lux;

import java.time.LocalDate;
import java.util.*;

/**
 * Generates the (month, day) pairs to pool for a given target calendar date.
 * Uses a fixed leap year as scratch space so Feb 29 and year-boundary
 * wraparound (e.g. Dec 28 -> Jan 3) fall out of plain date arithmetic
 * instead of needing special-cased handling.
 */
public final class DayWindow
{

	private static final int LEAP_YEAR = 2020;

	private DayWindow()
	{
	}

	public record MonthDay(int month, int day)
	{
	}

	public static Set<MonthDay> forDate(int month, int day, int windowDays)
	{
		LocalDate center = LocalDate.of(LEAP_YEAR, month, day);
		Set<MonthDay> pairs = new LinkedHashSet<>();
		for (int offset = -windowDays; offset <= windowDays; offset++)
		{
			LocalDate d = center.plusDays(offset);
			pairs.add(new MonthDay(d.getMonthValue(), d.getDayOfMonth()));
		}
		return pairs;
	}
}