package org.beni.gestionboisson.lot.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.beni.gestionboisson.shared.response.ApiResponse;

@Provider
public class InvalidLotRequestExceptionMapper implements ExceptionMapper<InvalidLotRequestException> {
    @Override
    public Response toResponse(InvalidLotRequestException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.badRequest(exception.getMessage()))
                .build();
    }
}
