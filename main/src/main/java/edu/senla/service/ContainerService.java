package edu.senla.service;

import edu.senla.model.dto.ContainerComponentsDTO;
import edu.senla.model.dto.ContainerComponentsNamesDTO;
import edu.senla.model.dto.ContainerComponentsParamsDTO;
import edu.senla.model.entity.Container;
import edu.senla.model.entity.Order;

import java.util.List;

public interface ContainerService {

    List<ContainerComponentsDTO> filterContainers(List<ContainerComponentsDTO> containers);

    double calculateTotalOrderCost(List<Container> containers);

    ContainerComponentsParamsDTO calculateWeightOfDishes(String containerComponentsJson);

    ContainerComponentsNamesDTO mapFromContainerEntityToContainerComponentsNamesDTO(Container container);

    Container mapFromContainerComponentsDTOToContainerEntity(ContainerComponentsDTO containerComponentsDTO, Order order);
}
