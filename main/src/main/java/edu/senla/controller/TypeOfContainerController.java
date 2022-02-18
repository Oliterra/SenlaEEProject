package edu.senla.controller;

import edu.senla.model.dto.TypeOfContainerDTO;

import java.util.List;

public interface TypeOfContainerController {

    List<TypeOfContainerDTO> getAllTypesOfContainer(int pages);

    void createTypeOfContainer(String typeOfContainerJson);

    TypeOfContainerDTO getTypeOfContainer(long id);

    void updateTypeOfContainer(long id, String updatedTypeOfContainerJson);

    void deleteTypeOfContainer(long id);
}
