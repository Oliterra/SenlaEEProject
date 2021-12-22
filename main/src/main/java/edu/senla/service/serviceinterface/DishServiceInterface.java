package edu.senla.service.serviceinterface;

import edu.senla.dto.DishDTO;

public interface DishServiceInterface {

    public DishDTO createDish(DishDTO newDishDTO);

    public DishDTO readDish(long id);

    public DishDTO updateDish(long id, DishDTO updatedDishDTO);

    public void deleteDish(long id);

    public DishDTO getByIdWithFullInformation(long dishId);

    public boolean isDishExists(DishDTO dish);

}
