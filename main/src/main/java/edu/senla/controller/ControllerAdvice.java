package edu.senla.controller;

import com.fasterxml.jackson.core.JsonParseException;
import edu.senla.exeption.BadRequest;
import edu.senla.exeption.ConflictBetweenData;
import edu.senla.exeption.NotFound;
import edu.senla.exeption.UnexpectedInternalError;
import edu.senla.model.dto.ErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NotFound.class)
    public ErrorDTO notFound(NotFound notFound) {
        ResponseEntity.notFound();
        return new ErrorDTO(notFound.getMessage());
    }

    @ExceptionHandler(BadRequest.class)
    public ErrorDTO badRequest(BadRequest badRequest) {
        ResponseEntity.badRequest();
        return new ErrorDTO(badRequest.getMessage());
    }

    @ExceptionHandler(ConflictBetweenData.class)
    public ErrorDTO conflictBetweenData(ConflictBetweenData conflictBetweenData) {
        ResponseEntity.badRequest();
        return new ErrorDTO(conflictBetweenData.getMessage());
    }

    @ExceptionHandler(JsonParseException.class)
    public ErrorDTO jsonParseException() {
        ResponseEntity.badRequest();
        return new ErrorDTO("Invalid json request");
    }

    @ExceptionHandler(UnexpectedInternalError.class)
    public ErrorDTO unexpectedInternalError(UnexpectedInternalError unexpectedInternalError) {
        ResponseEntity.internalServerError();
        return new ErrorDTO(unexpectedInternalError.getMessage());
    }
}


