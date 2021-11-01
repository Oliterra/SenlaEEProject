package edu.senla.controller;

import edu.senla.di.annotations.Autowire;
import edu.senla.di.annotations.Component;
import edu.senla.service.ServiceInterface;

@Component
public class Controller implements ControllerInterface{

    @Autowire
    private ServiceInterface service;

    @Override
    public String doAction() {
        return service.doAction();
    }
}
