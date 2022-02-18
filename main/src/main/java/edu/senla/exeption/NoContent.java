package edu.senla.exeption;

public class NoContent extends RuntimeException{

    public String getMessage() {
        return "No content";
    }
}
