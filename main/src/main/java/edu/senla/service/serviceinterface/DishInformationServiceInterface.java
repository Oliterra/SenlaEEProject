package edu.senla.service.serviceinterface;

import edu.senla.dto.DishInformationDTO;
import edu.senla.dto.DishInformationForUpdateDTO;

import java.util.List;

public interface DishInformationServiceInterface {

    List<DishInformationDTO> getAllDishesInformation();

    void createDishInformation(DishInformationDTO newDishInformationDTO);

    DishInformationDTO getDishInformation(long id);

    void updateDishInformation(long id, DishInformationForUpdateDTO updatedDishInformationDTO);

    void deleteDishInformation(long id);

}
