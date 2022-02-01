package edu.senla.exeptions;

public class BadRequest extends RuntimeException{

    public BadRequest(String message) {
        super(message);
    }

    public BadRequest() { }

}
