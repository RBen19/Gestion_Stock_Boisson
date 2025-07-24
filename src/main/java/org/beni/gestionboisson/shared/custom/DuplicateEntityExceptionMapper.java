package org.beni.gestionboisson.shared.custom;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.beni.gestionboisson.shared.response.ApiResponse;

@Provider
public class DuplicateEntityExceptionMapper implements ExceptionMapper<DuplicateEntityException> {
    @Override
    public Response toResponse(DuplicateEntityException exception) {
        return Response.status(Response.Status.CONFLICT)
                .entity(ApiResponse.errorConflict(exception.getMessage()))
                .build();
    }
}
