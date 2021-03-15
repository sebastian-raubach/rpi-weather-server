package uk.co.raubach.weatherstation.server.util;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;

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
	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		PropertyWatcher.initialize();
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent)
	{
		PropertyWatcher.stopFileWatcher();
	}
}
