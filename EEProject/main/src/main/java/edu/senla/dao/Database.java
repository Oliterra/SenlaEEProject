package edu.senla.dao;

import edu.senla.di.annotations.Component;
import edu.senla.di.annotations.Value;

@Component
public class Database implements DatabaseInterface{

    @Value("my.param.db")
    private String someText;

    public String doAction(){
        return someText;
    }
}
