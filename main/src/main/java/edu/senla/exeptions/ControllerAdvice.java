package edu.senla.exeptions;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.dto.ErrorDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    private final ObjectMapper mapper;

    @SneakyThrows
    @ExceptionHandler(NotFound.class)
    public ResponseEntity<String> notFound(NotFound notFound) {
        ErrorDTO response = new ErrorDTO(notFound.getMessage());
        return new ResponseEntity<String>(mapper.writeValueAsString(response), HttpStatus.NOT_FOUND);
    }

    @SneakyThrows
    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<String> badRequest(BadRequest badRequest) {
        ErrorDTO response = new ErrorDTO(badRequest.getMessage());
        return new ResponseEntity<String>(mapper.writeValueAsString(response), HttpStatus.BAD_REQUEST);
    }

    @SneakyThrows
    @ExceptionHandler(ConflictBetweenData.class)
    public ResponseEntity<String> conflictBetweenData(ConflictBetweenData conflictBetweenData) {
        ErrorDTO response = new ErrorDTO(conflictBetweenData.getMessage());
        return new ResponseEntity<String>(mapper.writeValueAsString(response), HttpStatus.CONFLICT);
    }

    @SneakyThrows
    @ExceptionHandler(NoContent.class)
    public ResponseEntity<String> noContent(NoContent noContent) {
        ErrorDTO response = new ErrorDTO(noContent.getMessage());
        return new ResponseEntity<String>(mapper.writeValueAsString(response), HttpStatus.NOT_FOUND);
    }

    @SneakyThrows
    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<String> jsonParseException(JsonParseException jsonParseException) {
        ErrorDTO response = new ErrorDTO("Invalid json request");
        return new ResponseEntity<String>(mapper.writeValueAsString(response), HttpStatus.BAD_REQUEST);
    }

}


