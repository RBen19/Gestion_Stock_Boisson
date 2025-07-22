package org.beni.gestionboisson.fournisseur.exceptions;

public class FournisseurNotFoundException extends RuntimeException {
    public FournisseurNotFoundException(String message) {
        super(message);
    }
}
