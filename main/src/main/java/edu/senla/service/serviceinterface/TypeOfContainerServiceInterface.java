package edu.senla.service.serviceinterface;

import edu.senla.dto.OrderDTO;
import edu.senla.dto.TypeOfContainerDTO;
import edu.senla.entity.Order;
import edu.senla.entity.TypeOfContainer;

public interface TypeOfContainerServiceInterface {

    public void createTypeOfContainer(TypeOfContainerDTO newTypeOfContainerDTO);

    public TypeOfContainerDTO readTypeOfContainer(int id);

    public void updateTypeOfContainer(int id, TypeOfContainerDTO updatedTypeOfContainerDTO);

    public void deleteTypeOfContainer(int id);

}
