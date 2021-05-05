package uk.co.raubach.weatherstation.server;

import org.restlet.*;
import org.restlet.data.*;
import org.restlet.engine.application.*;
import org.restlet.resource.ServerResource;
import org.restlet.routing.*;
import org.restlet.service.EncoderService;
import org.restlet.util.Series;
import uk.co.raubach.weatherstation.server.resource.*;

import java.util.*;

/**
 * @author Sebastian Raubach
 */
public class WeatherStation extends Application
{
	public Router router;

	public WeatherStation()
	{
		// Set information about API
		setName("WeatherStation Server");
		setDescription("This is the server implementation for WeatherStation");
		setOwner("Sebastian Raubach");
		setAuthor("Sebastian Raubach");
	}

	@Override
	public Restlet createInboundRoot()
	{
		Context context = getContext();


		// Create new router
		router = new Router(context);

		// Set the encoder
		Filter encoder = new Encoder(context, false, true, new EncoderService(true));
		encoder.setNext(router);

		// Set the Cors filter
		CorsFilter corsFilter = new CorsFilter(context, encoder)
		{
			@Override
			protected int beforeHandle(Request request, Response response)
			{
				if (getCorsResponseHelper().isCorsRequest(request))
				{
					Series<Header> headers = request.getHeaders();

					for (Header header : headers)
					{
						if (header.getName().equalsIgnoreCase("origin"))
						{
							response.setAccessControlAllowOrigin(header.getValue());
						}
					}
				}
				return super.beforeHandle(request, response);
			}
		};
		corsFilter.setAllowedOrigins(new HashSet<>(Collections.singletonList("*")));
		corsFilter.setSkippingResourceForCorsOptions(true);
		corsFilter.setAllowingAllRequestedHeaders(true);
		corsFilter.setDefaultAllowedMethods(new HashSet<>(Arrays.asList(Method.POST, Method.GET, Method.PUT, Method.PATCH, Method.DELETE, Method.OPTIONS)));
		corsFilter.setAllowedCredentials(true);
		corsFilter.setExposedHeaders(Collections.singleton("Content-Disposition"));

		attachToRouter(router, "/data", DataResource.class);
		attachToRouter(router, "/stats/daily", DailyStatsResource.class);

		return corsFilter;
	}

	private void attachToRouter(Router router, String url, Class<? extends ServerResource> clazz)
	{
		router.attach(url, clazz);
		router.attach(url + "/", clazz);
	}
}
