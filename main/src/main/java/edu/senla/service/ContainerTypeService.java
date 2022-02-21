package edu.senla.service;

import edu.senla.model.dto.ContainerTypeDTO;
import edu.senla.model.entity.ContainerType;

import java.util.List;

public interface ContainerTypeService {

    List<ContainerTypeDTO> getAllTypesOfContainer(int pages);

    void createTypeOfContainer(String typeOfContainerJson);

    ContainerTypeDTO getTypeOfContainer(long id);

    void updateTypeOfContainer(long id, String updatedTypeOfContainerJson);

    void deleteTypeOfContainer(long id);

    boolean isContainerTypeExists(String name);

    ContainerType getTypeOfContainerByName(String name);
}
