package com.microservico.restaurantService.exceptions;


import com.microservico.restaurantService.dto.response.ExceptionDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDTO> handleDuplicateEntry () {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Usuario já cadastrado", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ExceptionDTO> handleRecursoNaoEcontrado(RecursoNaoEncontradoException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDTO);
    }

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<ExceptionDTO> handleProdutoNaoEcontrado(ProdutoNaoEncontradoException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDTO);
    }

    @ExceptionHandler(StatusIncorretoException.class)
    public ResponseEntity<ExceptionDTO> handleStatusIncorreto(StatusIncorretoException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
    }

}
