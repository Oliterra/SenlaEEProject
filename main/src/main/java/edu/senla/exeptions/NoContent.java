package edu.senla.exeptions;

public class NoContent extends RuntimeException{

    public String getMessage() {
        return "No content";
    }

}
