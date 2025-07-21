package org.beni.gestionboisson.emplacement.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class DuplicateEmplacementCodeException extends WebApplicationException {
    public DuplicateEmplacementCodeException(String message) {
        super(message, Response.Status.CONFLICT);
    }
}
