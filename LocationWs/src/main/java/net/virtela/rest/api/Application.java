package net.virtela.rest.api;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import com.wordnik.swagger.jaxrs.listing.SwaggerSerializers;
import com.wordnik.swagger.jersey.listing.ApiListingResourceJSON;

import net.virtela.filter.CORSResponseFilter;
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
		register(CORSResponseFilter.class); // Enable CORS
		
		// Enable JASON import and export
		register(JacksonFeature.class);
		
		// Registration of the applications Rest Services
		register(GeoRestApi.class);
		
		// Enable swagger
		register(ApiListingResourceJSON.class);
		register(SwaggerSerializers.class);
	}

}
