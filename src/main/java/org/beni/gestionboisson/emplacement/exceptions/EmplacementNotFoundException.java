package org.beni.gestionboisson.emplacement.exceptions;

public class EmplacementNotFoundException extends RuntimeException {
    public EmplacementNotFoundException(String message) {
        super(message);
    }
}
