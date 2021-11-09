package edu.senla.controller.controllerinterface;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface DishInformationControllerInterface {

    public void createDishInformation(String newDishInformationJson);

    public String readDishInformation(int id) throws JsonProcessingException;

    public void updateDishInformation(String dishInformationToUpdateJson, String updatedDishInformationJson);

    public void deleteDishInformation(int id);

}
