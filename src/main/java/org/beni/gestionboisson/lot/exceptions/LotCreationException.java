package org.beni.gestionboisson.lot.exceptions;

public class LotCreationException extends RuntimeException {
    public LotCreationException(String message) {
        super(message);
    }

    public LotCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
