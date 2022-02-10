package edu.senla.exeption;

public class ConflictBetweenData extends RuntimeException{

    public ConflictBetweenData(String message) {
        super(message);
    }
}
