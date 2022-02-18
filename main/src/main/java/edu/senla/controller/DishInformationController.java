package edu.senla.controller;

import edu.senla.model.dto.DishInformationDTO;

import java.util.List;

public interface DishInformationController {

    List<DishInformationDTO> getAllDishesInformation(int pages);

    void createDishInformation(String dishInformationJson);

    DishInformationDTO getDishInformation(long id);

    void updateDishInformation(long id, String updatedDishInformationJson);

    void deleteDishInformation(int id);
}
