package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.DishControllerInterface;
import edu.senla.dto.DishDTO;
import edu.senla.service.serviceinterface.DishServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DishController implements DishControllerInterface {

    private final DishServiceInterface dishService;

    private final ObjectMapper jacksonObjectMapper;

    @SneakyThrows
    @Override
    public int createDish(String newDishJson) {
        DishDTO newDishDTO = jacksonObjectMapper.readValue(newDishJson, DishDTO.class);
        int newDishId = dishService.createDish(newDishDTO);
        System.out.println("Dish" + readDish(getDishIdByName(newDishDTO.getName())) + " was successfully created");
        return newDishId;
    }

    @SneakyThrows
    @Override
    public String readDish(int id) {
        DishDTO dishDTO = dishService.readDish(id);
        return jacksonObjectMapper.writeValueAsString(dishDTO);
    }

    @SneakyThrows
    @Override
    public void updateDish(int id, String updatedDishJson) {
        DishDTO updatedDishDTO = jacksonObjectMapper.readValue(updatedDishJson, DishDTO.class);
        dishService.updateDish(id, updatedDishDTO);
        System.out.println("Dish was successfully updated");
    }

    @Override
    public void deleteDish(int id) {
        dishService.deleteDish(id);
        System.out.println("Dish was successfully deleted");
    }

    @Override
    public int getDishIdByName(String dishName) {
        return dishService.getDishIdByName(dishName);
    }

    @Override
    public String getByIdWithFullInformation(int dishId) {
        System.out.println("Dish with its info: ");
        return dishService.getByIdWithFullInformation(dishId).toString();
    }

}
