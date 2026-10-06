package com.vitorbarros.dscatalog.exeptions;

public class InvalidResourceException extends RuntimeException {
    public InvalidResourceException(String message) {
        super(message);
    }
}
