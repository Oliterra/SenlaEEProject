package edu.senla.service.serviceinterface;

import edu.senla.dto.DishInformationDTO;
import edu.senla.entity.DishInformation;

public interface DishInformationServiceInterface {

    public void createDishInformation(DishInformationDTO newDishInformationDTO);

    public DishInformationDTO read(int id);

    public DishInformation update(int id, DishInformationDTO updatedDishInformationDTO);

    public void delete(int id);

}
