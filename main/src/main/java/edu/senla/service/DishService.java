package edu.senla.service;

import edu.senla.model.dto.ContainerComponentsDTO;
import edu.senla.model.dto.DishDTO;

import java.util.List;

public interface DishService {

    List<DishDTO> getAllDishes(int pages);

    void createDish(String newDishJson);

    DishDTO getDish(long id);

    void updateDish(long id, String updatedDishJson);

    void deleteDish(long id);

    boolean isDishHasDishInformation(long id);

    boolean isAllDishesHaveDishInformation(ContainerComponentsDTO containerComponentsDTO);
}
