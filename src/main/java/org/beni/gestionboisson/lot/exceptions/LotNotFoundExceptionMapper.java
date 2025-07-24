package org.beni.gestionboisson.lot.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.beni.gestionboisson.shared.response.ApiResponse;

@Provider
public class LotNotFoundExceptionMapper implements ExceptionMapper<LotNotFoundException> {
    @Override
    public Response toResponse(LotNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(ApiResponse.notFound(exception.getMessage()))
                .build();
    }
}
