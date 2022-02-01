package edu.senla.service.serviceinterface;

import edu.senla.dto.*;
import edu.senla.entity.Container;
import edu.senla.entity.Order;

import java.util.List;

public interface ContainerServiceInterface {

    List<ContainerComponentsDTO> filterContainers(List<ContainerComponentsDTO> containers);

    double calculateTotalOrderCost(List<Container> containers);

    ContainerComponentsParamsDTO calculateWeightOfDishes(ContainerComponentsDTO containerComponentsDTO);

    ContainerComponentsNamesDTO mapFromContainerEntityToContainerComponentsNamesDTO(Container container);

    public Container mapFromContainerComponentsDTOToContainerEntity(ContainerComponentsDTO containerComponentsDTO, Order order);

}
