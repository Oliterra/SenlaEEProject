package edu.senla.controller;

import edu.senla.model.dto.ContainerTypeDTO;

import java.util.List;

public interface ContainerTypeController {

    List<ContainerTypeDTO> getAllTypesOfContainer(int pages);

    void createTypeOfContainer(String typeOfContainerJson);

    ContainerTypeDTO getTypeOfContainer(long id);

    void updateTypeOfContainer(long id, String updatedTypeOfContainerJson);

    void deleteTypeOfContainer(long id);
}
