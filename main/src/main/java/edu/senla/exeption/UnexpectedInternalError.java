package edu.senla.exeption;

public class UnexpectedInternalError extends RuntimeException{

    public UnexpectedInternalError(String message) {
        super(message);
    }
}
