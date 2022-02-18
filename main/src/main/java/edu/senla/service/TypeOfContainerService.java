package edu.senla.service;

import edu.senla.model.dto.TypeOfContainerDTO;
import edu.senla.model.entity.TypeOfContainer;

import java.util.List;

public interface TypeOfContainerService {

    List<TypeOfContainerDTO> getAllTypesOfContainer(int pages);

    void createTypeOfContainer(String typeOfContainerJson);

    TypeOfContainerDTO getTypeOfContainer(long id);

    void updateTypeOfContainer(long id, String updatedTypeOfContainerJson);

    void deleteTypeOfContainer(long id);

    boolean isTypeOfContainerExists(String name);

    TypeOfContainer getTypeOfContainerByName(String name);
}
