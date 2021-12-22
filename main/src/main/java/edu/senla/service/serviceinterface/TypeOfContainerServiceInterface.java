package edu.senla.service.serviceinterface;

import edu.senla.dto.TypeOfContainerDTO;

public interface TypeOfContainerServiceInterface {

    public TypeOfContainerDTO createTypeOfContainer(TypeOfContainerDTO newTypeOfContainerDTO);

    public TypeOfContainerDTO readTypeOfContainer(long id);

    public TypeOfContainerDTO updateTypeOfContainer(long id, TypeOfContainerDTO updatedTypeOfContainerDTO);

    public void deleteTypeOfContainer(long id);

    public boolean isTypeOfContainerExists(TypeOfContainerDTO typeOfContainer);

}
