package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.TypeOfContainerControllerInterface;
import edu.senla.dto.TypeOfContainerDTO;
import edu.senla.service.serviceinterface.TypeOfContainerServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TypeOfContainerController implements TypeOfContainerControllerInterface {

    private final TypeOfContainerServiceInterface typeOfContainerService;

    private final ObjectMapper jacksonObjectMapper;

    @SneakyThrows
    @Override
    public void createTypeOfContainer(String newTypeOfContainerJson) {
        TypeOfContainerDTO newTypeOfContainerDTO = jacksonObjectMapper.readValue(newTypeOfContainerJson, TypeOfContainerDTO.class);
        typeOfContainerService.createTypeOfContainer(newTypeOfContainerDTO);
        System.out.println("TypeOfContainer" + readTypeOfContainer(newTypeOfContainerDTO.getNumberOfCalories()) + " was successfully created");
    }

    @SneakyThrows
    @Override
    public String readTypeOfContainer(int id) {
        TypeOfContainerDTO typeOfContainerDTO = typeOfContainerService.read(id);
        return jacksonObjectMapper.writeValueAsString(typeOfContainerDTO);
    }

    @SneakyThrows
    @Override
    public void updateTypeOfContainer(int id, String updatedTypeOfContainerJson) {
        TypeOfContainerDTO updatedTypeOfContainerDTO = jacksonObjectMapper.readValue(updatedTypeOfContainerJson, TypeOfContainerDTO.class);
        typeOfContainerService.update(id, updatedTypeOfContainerDTO);
        System.out.println("TypeOfContainer was successfully updated");
    }

    @Override
    public void deleteTypeOfContainer(int id) {
        typeOfContainerService.delete(id);
        System.out.println("Type of container was successfully deleted");
    }

}
