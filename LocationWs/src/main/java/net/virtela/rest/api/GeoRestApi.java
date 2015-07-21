package net.virtela.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.virtela.model.Country;
import net.virtela.service.GeoService;

@Component
@Path("/geo_location")
public class GeoRestApi {
	
	@Autowired
	private GeoService geoService;
	
	/**
	 * Returns all resources (Country) from the database
	 * 
	 * Response: 
	 *    200 - Data was found. 
	 *    401 - User has no Access 
	 *    404 - There was no date found.
	 * 
	 * @return
	 */
	@GET
	@Path("v1.0/countries")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCountries() {
		return Response.status(Status.OK).entity(this.geoService.getCountries()).build();
	}
	
	/**
	 * Returns a resources (Country) from the database based on the ID given
	 * 
	 * Response: 
	 *    200 - Data was found. 
	 *    401 - User has no Access 
	 *    404 - There was no date found.
	 * 
	 * @return
	 */
	@GET
	@Path("v1.0/countries/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCountries(@PathParam("id") Long id) {
		final Country country = this.geoService.getCountryById(id);
		if (country != null) {
			return Response.status(Status.OK).entity(this.geoService.getCountryById(id)).build();
		} 
		return Response.status(Status.NOT_FOUND).entity("Record not Found").build();	
		
	}

}
