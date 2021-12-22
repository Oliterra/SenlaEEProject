package edu.senla.service.serviceinterface;

import edu.senla.dto.DishInformationDTO;

public interface DishInformationServiceInterface {

    public DishInformationDTO createDishInformation(long dishId, DishInformationDTO newDishInformationDTO);

    public DishInformationDTO readDishInformation(long id);

    public DishInformationDTO updateDishInformation(long id, DishInformationDTO updatedDishInformationDTO);

    public void deleteDishInformation(long id);

}
