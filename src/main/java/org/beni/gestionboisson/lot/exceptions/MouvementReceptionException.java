package org.beni.gestionboisson.lot.exceptions;

public class MouvementReceptionException extends RuntimeException {
    public MouvementReceptionException(String message) {
        super(message);
    }

    public MouvementReceptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
