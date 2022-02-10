package edu.senla.exeption;

import com.fasterxml.jackson.core.JsonParseException;
import edu.senla.model.dto.ErrorDTO;
import lombok.SneakyThrows;
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

    @SneakyThrows
    @ExceptionHandler(ConflictBetweenData.class)
    public ErrorDTO conflictBetweenData(ConflictBetweenData conflictBetweenData) {
        ResponseEntity.badRequest();
        return new ErrorDTO(conflictBetweenData.getMessage());
    }

    @SneakyThrows
    @ExceptionHandler(JsonParseException.class)
    public ErrorDTO jsonParseException() {
        ResponseEntity.badRequest();
        return new ErrorDTO("Invalid json request");
    }
}


