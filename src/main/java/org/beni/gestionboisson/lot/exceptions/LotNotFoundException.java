package org.beni.gestionboisson.lot.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.beni.gestionboisson.shared.response.ApiResponse;

public class LotNotFoundException extends RuntimeException {
    public LotNotFoundException(String message) {
        super(message);
    }
}
