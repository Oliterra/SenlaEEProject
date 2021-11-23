package edu.senla.service.serviceinterface;

import edu.senla.dto.DishDTO;
import edu.senla.entity.Dish;

public interface DishServiceInterface {

    public void createDish(DishDTO newDishDTO);

    public DishDTO readDish(int id);

    public void updateDish(int id, DishDTO updatedDishDTO);

    public void deleteDish(int id);

    public int getDishIdByName(String dishName);

}
