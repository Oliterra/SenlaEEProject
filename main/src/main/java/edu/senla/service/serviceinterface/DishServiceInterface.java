package edu.senla.service.serviceinterface;

import edu.senla.dto.DishDTO;

public interface DishServiceInterface {

    public DishDTO createDish(DishDTO newDishDTO);

    public DishDTO readDish(int id);

    public DishDTO updateDish(int id, DishDTO updatedDishDTO);

    public void deleteDish(int id);

    public DishDTO getByIdWithFullInformation(int dishId);

    public boolean isDishExists(DishDTO dish);

}
