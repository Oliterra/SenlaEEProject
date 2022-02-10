package edu.senla.service;

import edu.senla.model.dto.DishInformationDTO;

import java.util.List;

public interface DishInformationService {

    List<DishInformationDTO> getAllDishesInformation(int pages);

    void createDishInformation(String dishInformationJson);

    DishInformationDTO getDishInformation(long id);

    void updateDishInformation(long id, String updatedDishInformationJson);

    void deleteDishInformation(long id);
}
