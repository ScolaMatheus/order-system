package com.microservico.order.exceptions;

public class RecursoNaoEncontradoException extends RuntimeException{
    public RecursoNaoEncontradoException(String message) {
        super(message);
    }
}
