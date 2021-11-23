package edu.senla.controller.controllerinterface;

public interface DishControllerInterface {

    public void createDish(String newDishJson);

    public String readDish(int id);

    public void updateDish(int id, String updatedDishJson);

    public void deleteDish(int id);

    public int getDishIdByName(String dishName);

}
