package org.beni.gestionboisson.emplacement.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.beni.gestionboisson.shared.response.ApiResponse;

@Provider
public class DuplicateEmplacementCodeExceptionMapper implements ExceptionMapper<DuplicateEmplacementCodeException> {

    @Override
    public Response toResponse(DuplicateEmplacementCodeException exception) {
        return Response.status(Response.Status.CONFLICT)
                .entity(ApiResponse.error(exception.getMessage(), 409))
                .build();
    }
}
