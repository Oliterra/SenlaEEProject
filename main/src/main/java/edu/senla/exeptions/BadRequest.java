package edu.senla.exeptions;

public class BadRequest extends RuntimeException{

    public String getMessage() {
        return "Bad request";
    }

    public int getStatus() {
        return 400;
    }

}
