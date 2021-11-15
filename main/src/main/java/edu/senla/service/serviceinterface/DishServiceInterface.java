package edu.senla.service.serviceinterface;

import edu.senla.dto.DishDTO;
import edu.senla.entity.Dish;

public interface DishServiceInterface {

    public void createDish(DishDTO newDishDTO);

    public DishDTO read(int id);

    public Dish update(int id, DishDTO updatedDishDTO);

    public void delete(int id);

}
