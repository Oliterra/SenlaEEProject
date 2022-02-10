package edu.senla.service;

import edu.senla.model.dto.DishInformationDTO;
import edu.senla.model.dto.DishInformationForUpdateDTO;

import java.util.List;

public interface DishInformationService {

    List<DishInformationDTO> getAllDishesInformation(int pages);

    void createDishInformation(DishInformationDTO newDishInformationDTO);

    DishInformationDTO getDishInformation(long id);

    void updateDishInformation(long id, DishInformationForUpdateDTO updatedDishInformationDTO);

    void deleteDishInformation(long id);
}
