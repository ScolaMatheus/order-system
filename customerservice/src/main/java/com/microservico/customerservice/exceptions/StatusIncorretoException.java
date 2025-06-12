package com.microservico.customerservice.exceptions;

public class StatusIncorretoException extends RuntimeException {
    public StatusIncorretoException(String message) {
        super(message);
    }
}
