package net.virtela.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import net.virtela.constants.AppMessage;
import net.virtela.model.Country;
import net.virtela.model.ErrorMessages;
import net.virtela.service.GeoService;

@Component
@Path("/geo_location")
@Api(value = "Geo Location", description = "Service for locations")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class GeoRestApi {
	
	@Autowired
	private GeoService geoService;

	@GET
	@Path("v1.0/country/list/")
	@ApiOperation(value = "Get All Country", notes = "Get All avaialble county in the database", response = Country.class
	              , responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = AppMessage.ERROR_DATA_NOT_FOUND, response = ErrorMessages.class)
	                        , @ApiResponse(code = 401, message = AppMessage.ERROR_UNAUTHORIZED_ACCESS, response = ErrorMessages.class) })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCountries() {
		return Response.status(Status.OK).entity(this.geoService.getCountries()).build();
	}
	
	
	@GET
	@Path("v1.0/country/{id}")
	@ApiOperation(value = "Get a Country by ID", notes = "Get a Spesifict Country via Primary Key", response = Country.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = AppMessage.ERROR_DATA_NOT_FOUND, response = ErrorMessages.class)
	                        , @ApiResponse(code = 401, message = AppMessage.ERROR_UNAUTHORIZED_ACCESS, response = ErrorMessages.class) })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCountryById(@PathParam("id") Long id) {
		final Country country = this.geoService.getCountryById(id);
		if (country != null) {
			return Response.status(Status.OK).entity(this.geoService.getCountryById(id)).build();
		} 
		return Response.status(Status.NOT_FOUND).entity("Record not Found").build();	
		
	}
	
	@POST
	@Path("v1.0/countries")
	@ApiOperation(value = "Create Country", notes = "Inserts resources Country to the database", response = Country.class)
    @ApiResponses(value = { @ApiResponse(code = 404, message = AppMessage.ERROR_DATA_NOT_FOUND, response = ErrorMessages.class)
                  , @ApiResponse(code = 401, message = AppMessage.ERROR_UNAUTHORIZED_ACCESS, response = ErrorMessages.class)
                  , @ApiResponse(code = 406, message = AppMessage.ERROR_INVALID_PARAMETER, response = ErrorMessages.class)})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Transactional
	public Response createCountry(Country country) {
		if (this.geoService.createCountry(country) > 0) {
			return Response.status(Status.OK).entity("Record has been created").build();
		} 
		return Response.status(Status.BAD_REQUEST).entity("Insert Failed").build();	
		
	}
	
	/**
	 * Updates resources (Country) to the database
	 * 
	 * Response: 
	 *    200 - Record was Created. 
	 *    401 - User has no Access 
	 *    406 - Request Failed.
	 * 
	 * @return
	 */
	@PUT
	@Path("v1.0/countries/{id}")
	@ApiOperation(value = "Update Country by ID", notes = "Updates resources Country to the database via ID", response = Country.class)
    @ApiResponses(value = { @ApiResponse(code = 404, message = AppMessage.ERROR_DATA_NOT_FOUND, response = ErrorMessages.class)
                  , @ApiResponse(code = 401, message = AppMessage.ERROR_UNAUTHORIZED_ACCESS, response = ErrorMessages.class)
                  , @ApiResponse(code = 406, message = AppMessage.ERROR_INVALID_PARAMETER, response = ErrorMessages.class)})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Transactional
	public Response updateCountry(@PathParam("id") Long id, Country country) {
		if (this.geoService.updateCountry(country, id) > 0) {
			return Response.status(Status.OK).entity("Record has been updated").build();
		} 
		return Response.status(Status.BAD_REQUEST).entity("Updated Failed").build();	
		
	}
	
	@DELETE
	@Path("v1.0/countries/{id}")
	@ApiOperation(value = "Delete Country by ID", notes = "Delete resources Country to the database via ID", response = Country.class)
    @ApiResponses(value = { @ApiResponse(code = 404, message = AppMessage.ERROR_DATA_NOT_FOUND, response = ErrorMessages.class)
                  , @ApiResponse(code = 401, message = AppMessage.ERROR_UNAUTHORIZED_ACCESS, response = ErrorMessages.class)
                  , @ApiResponse(code = 406, message = AppMessage.ERROR_INVALID_PARAMETER, response = ErrorMessages.class)})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Transactional
	public Response deleteCountry(@PathParam("id") Long id) {
		if (this.geoService.deleteCountryById(id) > 0) {
			return Response.status(Status.OK).entity("Record has been deleted").build();
		} 
		return Response.status(Status.BAD_REQUEST).entity("Delete Failed").build();	
		
	}


}
