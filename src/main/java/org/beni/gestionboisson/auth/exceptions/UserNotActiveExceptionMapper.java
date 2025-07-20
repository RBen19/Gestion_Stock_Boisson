package org.beni.gestionboisson.auth.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UserNotActiveExceptionMapper implements ExceptionMapper<UserNotActiveException> {

    @Override
    public Response toResponse(UserNotActiveException exception) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(exception.getMessage()).build();
    }
}
