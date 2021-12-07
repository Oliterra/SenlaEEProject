package edu.senla.service.serviceinterface;

import edu.senla.dto.TypeOfContainerDTO;

public interface TypeOfContainerServiceInterface {

    public TypeOfContainerDTO createTypeOfContainer(TypeOfContainerDTO newTypeOfContainerDTO);

    public TypeOfContainerDTO readTypeOfContainer(int id);

    public TypeOfContainerDTO updateTypeOfContainer(int id, TypeOfContainerDTO updatedTypeOfContainerDTO);

    public void deleteTypeOfContainer(int id);

    public boolean isTypeOfContainerExists(TypeOfContainerDTO typeOfContainer);

}
