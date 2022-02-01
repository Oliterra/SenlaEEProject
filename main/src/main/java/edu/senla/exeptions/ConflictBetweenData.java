package edu.senla.exeptions;

public class ConflictBetweenData extends RuntimeException{

    public ConflictBetweenData(String message) {
        super(message);
    }

}
