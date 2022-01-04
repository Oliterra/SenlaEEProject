package edu.senla.exeptions;

public class ConflictBetweenData extends RuntimeException{

    public String getMessage() {
        return "Conflict between data";
    }

    public int getStatus() {
        return 409;
    }

}
