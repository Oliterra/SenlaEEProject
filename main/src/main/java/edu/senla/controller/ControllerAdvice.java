package edu.senla.controller;

import edu.senla.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO notFoundError(IllegalArgumentException exception, WebRequest request) {

        return new ErrorDTO(HttpStatus.NOT_FOUND, "Not found");

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO conflictError(IllegalArgumentException exception, WebRequest request) {

        return new ErrorDTO(HttpStatus.CONFLICT, "Conflict between data");

    }

}

