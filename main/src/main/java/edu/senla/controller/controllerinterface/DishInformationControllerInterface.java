package edu.senla.controller.controllerinterface;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface DishInformationControllerInterface {

    public void createDishInformation(int dishId, String newDishInformationJson);

    public String readDishInformation(int id) throws JsonProcessingException;

    public void updateDishInformation(int id, String updatedDishInformationJson);

    public void deleteDishInformation(int id);

}
