package com.microservico.OrderManagement.exceptions;

public class RecursoNaoEncontradoException extends RuntimeException{
    public RecursoNaoEncontradoException(String message) {
        super(message);
    }
}
