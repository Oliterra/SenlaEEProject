package edu.senla.service.serviceinterface;

import edu.senla.dto.DishInformationDTO;
import edu.senla.entity.DishInformation;

public interface DishInformationServiceInterface {

    public void createDishInformation(DishInformationDTO newDishInformationDTO);

    public DishInformationDTO readDishInformation(int id);

    public void updateDishInformation(int id, DishInformationDTO updatedDishInformationDTO);

    public void deleteDishInformation(int id);

}
