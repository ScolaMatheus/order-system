package com.microservico.customer.exceptions;

public class StatusIncorretoException extends RuntimeException {
    public StatusIncorretoException(String message) {
        super(message);
    }
}
