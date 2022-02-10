package edu.senla.controller;

import edu.senla.model.dto.DishDTO;

import java.util.List;

public interface DishController {

    List<DishDTO> getAllDishes(int pages);

    void createDish(String dishJson);

    DishDTO getDish(long id);

    void updateDish(long id, String updatedDishJson);

    void deleteDish(long id);
}
