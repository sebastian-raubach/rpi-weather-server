package uk.co.raubach.weatherstation.server.util.lux;

import java.util.List;

public final class SlotStats
{

	private SlotStats()
	{
	}

	/**
	 * Linear-interpolation percentile, matching the convention used by
	 * numpy.percentile's default ("linear") method.
	 */
	public static double percentile(List<Double> values, double pct)
	{
		double[] sorted = values.stream().mapToDouble(Double::doubleValue).sorted().toArray();
		int n = sorted.length;
		if (n == 0) return Double.NaN;
		if (n == 1) return sorted[0];

		double rank = (pct / 100.0) * (n - 1);
		int lower = (int) Math.floor(rank);
		int upper = (int) Math.ceil(rank);
		if (lower == upper) return sorted[lower];
		double frac = rank - lower;
		return sorted[lower] + frac * (sorted[upper] - sorted[lower]);
	}

	/**
	 * 9-point quadratic/cubic Savitzky-Golay smoothing kernel (standard
	 * Numerical-Recipes table), applied circularly across a day's slots.
	 * A day's lux curve is close to zero at both midnight boundaries, so
	 * wrap-around is a reasonable stand-in for scipy's
	 * savgol_filter(window_length=9, polyorder=2, mode="wrap").
	 */
	private static final double[] SG_KERNEL = {-21, 14, 39, 54, 59, 54, 39, 14, -21};
	private static final double   SG_NORM   = 231.0;

	public static double[] smoothCircular(double[] values)
	{
		int n = values.length;
		int half = SG_KERNEL.length / 2;
		double[] out = new double[n];
		for (int i = 0; i < n; i++)
		{
			double acc = 0;
			for (int k = -half; k <= half; k++)
			{
				int idx = Math.floorMod(i + k, n);
				acc += values[idx] * SG_KERNEL[k + half];
			}
			out[i] = Math.max(0.0, acc / SG_NORM);
		}
		return out;
	}

	/**
	 * Fills any NaN (no-data) slots by averaging the nearest available
	 * value on each side, wrapping around the day if needed.
	 */
	public static void fillGaps(double[] values)
	{
		int n = values.length;
		for (int i = 0; i < n; i++)
		{
			if (!Double.isNaN(values[i])) continue;

			int prev = i - 1;
			int steps = 0;
			while (steps < n && Double.isNaN(values[Math.floorMod(prev, n)]))
			{
				prev--;
				steps++;
			}
			int next = i + 1;
			steps = 0;
			while (steps < n && Double.isNaN(values[Math.floorMod(next, n)]))
			{
				next++;
				steps++;
			}
			double prevVal = values[Math.floorMod(prev, n)];
			double nextVal = values[Math.floorMod(next, n)];
			values[i] = (prevVal + nextVal) / 2.0;
		}
	}
}
