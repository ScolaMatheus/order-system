package com.microservico.restaurant.exceptions;

public class RecursoNaoEncontradoException extends RuntimeException{
    public RecursoNaoEncontradoException(String message) {
        super(message);
    }
}
