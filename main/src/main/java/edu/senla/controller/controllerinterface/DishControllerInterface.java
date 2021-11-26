package edu.senla.controller.controllerinterface;

import edu.senla.dto.DishDTO;

public interface DishControllerInterface {

    public int createDish(String newDishJson);

    public String readDish(int id);

    public void updateDish(int id, String updatedDishJson);

    public void deleteDish(int id);

    public int getDishIdByName(String dishName);

    public String getByIdWithFullInformation(int dishId);

}
