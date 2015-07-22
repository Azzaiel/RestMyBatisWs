package net.virtela.rest.api;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import net.virtela.filter.JSONRequestFilter;

public class Application extends ResourceConfig {
	/**
	 * Register JAX-RS application components.
	 */
	public Application() {
		// Connect Jersy to Spring
		register(RequestContextFilter.class);
		// Filter - executes before api service gets the request
		register(JSONRequestFilter.class); // Exception handler for miss formated request
		// Enable JASON import and export
		register(JacksonFeature.class);
		// Registration of the applications Rest Services
		register(GeoRestApi.class);
	}

}
