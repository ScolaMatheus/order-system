package com.microservico.restaurantService.exceptions;

public class StatusIncorretoException extends RuntimeException {
    public StatusIncorretoException(String message) {
        super(message);
    }
}
