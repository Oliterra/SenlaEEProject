package edu.senla.exeptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.dto.ErrorDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    private final ObjectMapper mapper;

    @SneakyThrows
    @ExceptionHandler(NotFound.class)
    public ResponseEntity<String> notFound(NotFound notFound) {
        ErrorDTO response = new ErrorDTO(notFound.getStatus(), notFound.getMessage());
        return new ResponseEntity<String>(mapper.writeValueAsString(response), HttpStatus.NOT_FOUND);
    }

    @SneakyThrows
    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<String> badRequest(BadRequest badRequest) {
        ErrorDTO response = new ErrorDTO(badRequest.getStatus(), badRequest.getMessage());
        return new ResponseEntity<String>(mapper.writeValueAsString(response), HttpStatus.BAD_REQUEST);
    }

    @SneakyThrows
    @ExceptionHandler(ConflictBetweenData.class)
    public ResponseEntity<String> conflictBetweenData(ConflictBetweenData conflictBetweenData) {
        ErrorDTO response = new ErrorDTO(conflictBetweenData.getStatus(), conflictBetweenData.getMessage());
        return new ResponseEntity<String>(mapper.writeValueAsString(response), HttpStatus.CONFLICT);
    }

    @SneakyThrows
    @ExceptionHandler(NoContent.class)
    public ResponseEntity<String> noContent(NoContent noContent) {
        ErrorDTO response = new ErrorDTO(noContent.getStatus(), noContent.getMessage());
        return new ResponseEntity<String>(mapper.writeValueAsString(response), HttpStatus.NO_CONTENT);
    }

    @SneakyThrows
    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<String> internalServerError() {
        ErrorDTO response = new ErrorDTO(418, "I am a teapot");
        return new ResponseEntity<String>(mapper.writeValueAsString(response), HttpStatus.I_AM_A_TEAPOT);
    }

}

