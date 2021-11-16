package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.DishInformationControllerInterface;
import edu.senla.dto.DishInformationDTO;
import edu.senla.service.serviceinterface.DishInformationServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DishInformationController implements DishInformationControllerInterface {

    private final DishInformationServiceInterface dishInformationService;

    private final ObjectMapper jacksonObjectMapper;

    @SneakyThrows
    @Override
    public void createDishInformation(String newDishInformationJson) {
        DishInformationDTO newDishInformationDTO = jacksonObjectMapper.readValue(newDishInformationJson, DishInformationDTO.class);
        dishInformationService.createDishInformation(newDishInformationDTO);
        System.out.println("DishInformation" + readDishInformation(newDishInformationDTO.getId()) + " was successfully created");
    }

    @SneakyThrows
    @Override
    public String readDishInformation(int id) {
        DishInformationDTO dishInformationDTO = dishInformationService.read(id);
        return jacksonObjectMapper.writeValueAsString(dishInformationDTO);
    }

    @SneakyThrows
    @Override
    public void updateDishInformation(int id, String updatedDishInformationJson) {
        DishInformationDTO updatedDishInformationDTO = jacksonObjectMapper.readValue(updatedDishInformationJson, DishInformationDTO.class);
        dishInformationService.update(id, updatedDishInformationDTO);
        System.out.println("DishInformation was successfully updated");
    }

    @Override
    public void deleteDishInformation(int id) {
        dishInformationService.delete(id);
        System.out.println("Dish information was successfully deleted");
    }

}
