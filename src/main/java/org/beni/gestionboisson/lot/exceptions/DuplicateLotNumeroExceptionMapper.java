package org.beni.gestionboisson.lot.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.beni.gestionboisson.shared.response.ApiResponse;

@Provider
public class DuplicateLotNumeroExceptionMapper implements ExceptionMapper<DuplicateLotNumeroException> {
    @Override
    public Response toResponse(DuplicateLotNumeroException exception) {
        return Response.status(Response.Status.CONFLICT)
                .entity(ApiResponse.errorConflict(exception.getMessage()))
                .build();
    }
}
