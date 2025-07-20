package org.beni.gestionboisson.auth.exceptions;

public class PasswordChangeRequiredException extends RuntimeException {
    public PasswordChangeRequiredException(String message) {
        super(message);
    }
}
