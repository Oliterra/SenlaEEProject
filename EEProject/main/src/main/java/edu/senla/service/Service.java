package edu.senla.service;

import edu.senla.dao.DatabaseInterface;
import edu.senla.di.annotations.Component;
import edu.senla.di.annotations.Autowire;

@Component
public class Service implements ServiceInterface{

    @Autowire
    private DatabaseInterface database;

    public String doAction(){
        return database.doAction();
    }

}
