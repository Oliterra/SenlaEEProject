package edu.senla.controller.controllerinterface;

public interface DishControllerInterface {

    public void createDish(String newDishJson);

    public String readDish(int id);

    public void updateDish(String dishToUpdateJson, String updatedDishJson);

    public void deleteDish(int id);

}
