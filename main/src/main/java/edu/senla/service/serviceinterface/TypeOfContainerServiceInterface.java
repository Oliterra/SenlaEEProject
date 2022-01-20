package edu.senla.service.serviceinterface;

import edu.senla.dto.TypeOfContainerDTO;
import edu.senla.dto.TypeOfContainerForUpdateDTO;
import edu.senla.entity.TypeOfContainer;

import java.util.List;

public interface TypeOfContainerServiceInterface {

    List<TypeOfContainerDTO> getAllTypesOfContainer();

    void createTypeOfContainer(TypeOfContainerDTO newTypeOfContainerDTO);

    TypeOfContainerDTO getTypeOfContainer(long id);

    void updateTypeOfContainer(long id, TypeOfContainerForUpdateDTO updatedTypeOfContainerDTO);

    void deleteTypeOfContainer(long id);

    boolean isTypeOfContainerExists(String name);

    TypeOfContainer getTypeOfContainerByName(String name);

}
