package edu.senla.service.serviceinterface;

import edu.senla.dto.ContainerComponentsDTO;
import edu.senla.dto.DishDTO;

import java.util.List;

public interface DishServiceInterface {

    List<DishDTO> getAllDishes();

    void createDish(DishDTO newDishDTO);

    DishDTO getDish(long id);

    void updateDish(long id, DishDTO updatedDishDTO);

    void deleteDish(long id);

    boolean isDishExists(Long id);

    boolean isDishExists(String name);

    boolean isDishHasDishInformation(long id);

    boolean isDishTypeCorrect(String dishType);

    boolean isAllDishesHaveDishInformation(ContainerComponentsDTO containerComponentsDTO);

}
