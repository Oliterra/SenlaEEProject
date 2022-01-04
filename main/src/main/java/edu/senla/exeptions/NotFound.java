package edu.senla.exeptions;

public class NotFound extends RuntimeException{

    public String getMessage() {
        return "Not found";
    }

    public int getStatus() {
        return 404;
    }

}
