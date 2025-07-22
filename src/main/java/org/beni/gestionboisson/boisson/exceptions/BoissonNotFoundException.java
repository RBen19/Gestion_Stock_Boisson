package org.beni.gestionboisson.boisson.exceptions;

public class BoissonNotFoundException extends RuntimeException {
    public BoissonNotFoundException(String message) {
        super(message);
    }
}
