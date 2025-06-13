package com.microservico.restaurant.exceptions;

public class StatusIncorretoException extends RuntimeException {
    public StatusIncorretoException(String message) {
        super(message);
    }
}
