package edu.senla.service.serviceinterface;

import edu.senla.dto.OrderDTO;
import edu.senla.dto.TypeOfContainerDTO;
import edu.senla.entity.Order;
import edu.senla.entity.TypeOfContainer;

public interface TypeOfContainerServiceInterface {

    public void createTypeOfContainer(TypeOfContainerDTO newTypeOfContainerDTO);

    public TypeOfContainerDTO read(int id);

    public TypeOfContainer update(int id, TypeOfContainerDTO updatedTypeOfContainerDTO);

    public void delete(int id);

}
