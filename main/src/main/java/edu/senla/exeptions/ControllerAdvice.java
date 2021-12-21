package edu.senla.exeptions;

/*@RestControllerAdvice
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

    @ExceptionHandler({AuthenticationException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO unauthorizedError(AuthenticationException exception) {

        return new ErrorDTO(HttpStatus.UNAUTHORIZED, "Unauthorized");

    }

}*/

