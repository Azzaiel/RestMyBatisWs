package net.virtela.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import net.virtela.model.LoginCredential;
import net.virtela.model.AuthenticationRequest;
import net.virtela.model.ErrorMessages;

@Component
@Path("/auth")
@Api(value = "Auth Service", description = "Authentication and Authorization Service")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class AuthRestApi {

	@ApiOperation(value = "Google Login", notes = "Auth", response = LoginCredential.class)
	@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized Access", response = ErrorMessages.class),
	        @ApiResponse(code = 406, message = "Invalid ID parameter", response = ErrorMessages.class) })
	@POST
	@Path("v1.0/user/google/login")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response login(@ApiParam(value = "Username and Password Container", required = true) AuthenticationRequest authRequest) {
		
		return null;
	}

}
