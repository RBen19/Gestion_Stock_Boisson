package org.beni.gestionboisson.auth.exceptions;

public class InvalidOldPasswordException extends RuntimeException {
    public InvalidOldPasswordException(String message) {
        super(message);
    }
}
