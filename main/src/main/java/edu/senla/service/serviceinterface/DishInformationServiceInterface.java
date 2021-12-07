package edu.senla.service.serviceinterface;

import edu.senla.dto.DishInformationDTO;

public interface DishInformationServiceInterface {

    public DishInformationDTO createDishInformation(int dishId, DishInformationDTO newDishInformationDTO);

    public DishInformationDTO readDishInformation(int id);

    public DishInformationDTO updateDishInformation(int id, DishInformationDTO updatedDishInformationDTO);

    public void deleteDishInformation(int id);

}
