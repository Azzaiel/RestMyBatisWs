package net.virtela.rest.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.virtela.model.Country;

import org.springframework.stereotype.Component;

@Component
@Path("/geo_location")
public class GeoRestApi {
	
	/**
	 * Returns all resources (Country) from the database
	 * 
	 * Response: 200 - Data was found. 401 - User has no Access 404 - There was no date found.
	 * 
	 * @return
	 */
	@GET
	@Path("v1.0/countries")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCountries() {
		final List<Country> countryList = new ArrayList<>();
		
		countryList.add(new Country(1l, "Japan", "JP", "System", new Date()));
		countryList.add(new Country(2l, "United States", "US", "System", new Date()));
		
		return Response.status(Status.NOT_FOUND).entity(countryList).build();
	}

}
