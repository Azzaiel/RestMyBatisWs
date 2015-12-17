package net.virtela.filter;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.codehaus.jackson.JsonParseException;

public class JSONRequestFilter implements ExceptionMapper<JsonParseException> {

	@Override
	public Response toResponse(JsonParseException exception) {
		exception.printStackTrace();
		return Response.status(Status.BAD_REQUEST).entity("Request jason format is invalid").build();
	}

}
