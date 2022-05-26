package uk.co.raubach.weatherstation.server.util;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;
import org.jooq.tools.StringUtils;

import java.util.concurrent.*;

/**
 * The {@link ApplicationListener} is the main {@link ServletContextListener} of the application. It's started when the application is loaded by
 * Tomcat. It contains {@link #contextInitialized(ServletContextEvent)} which is executed on start and {@link #contextDestroyed(ServletContextEvent)}
 * which is executed when the application terminates.
 *
 * @author Sebastian Raubach
 */
@WebListener
public class ApplicationListener implements ServletContextListener
{
	private static ScheduledExecutorService backgroundScheduler;

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		PropertyWatcher.initialize();

		backgroundScheduler = Executors.newSingleThreadScheduledExecutor();
		backgroundScheduler.scheduleAtFixedRate(new WUUploaderThread(), 1, 10, TimeUnit.MINUTES);
		backgroundScheduler.scheduleAtFixedRate(new AggregatedCalculatorThread(), 0, 3, TimeUnit.HOURS);

		if (!StringUtils.isEmpty(PropertyWatcher.get("latitude")) && !StringUtils.isEmpty(PropertyWatcher.get("longitude")) && !StringUtils.isEmpty("openweathermap.key"))
			backgroundScheduler.scheduleAtFixedRate(new ForecastThread(), 0, 1, TimeUnit.HOURS);
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent)
	{
		PropertyWatcher.stopFileWatcher();

		try
		{
			// Stop the scheduler
			if (backgroundScheduler != null)
				backgroundScheduler.shutdownNow();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
