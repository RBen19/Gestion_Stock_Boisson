package org.beni.gestionboisson.auth.exceptions;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
