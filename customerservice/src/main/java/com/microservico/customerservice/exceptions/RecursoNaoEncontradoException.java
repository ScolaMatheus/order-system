package com.microservico.customerservice.exceptions;

public class RecursoNaoEncontradoException extends RuntimeException{
    public RecursoNaoEncontradoException(String message) {
        super(message);
    }
}
