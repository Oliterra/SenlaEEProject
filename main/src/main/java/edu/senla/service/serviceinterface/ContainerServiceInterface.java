package edu.senla.service.serviceinterface;

import edu.senla.dto.*;
import edu.senla.entity.Container;

import java.util.List;

public interface ContainerServiceInterface {

    List<ContainerComponentsDTO> filterContainers(List<ContainerComponentsDTO> containers);

    OrderTotalCostDTO makeOrder(long clientId, ShoppingCartDTO shoppingCartDTO);

    boolean isPaymentTypeCorrect(String paymentType);

    ContainerComponentsNamesDTO mapFromContainerEntityToContainerComponentsNamesDTO(Container container);

    double calculateTotalOrderCost(List<Container> containers);

    boolean isContainerComponentsCorrect(ContainerComponentsDTO containerComponentsDTO);

    ContainerComponentsParamsDTO calculateWeightOfDishes(ContainerComponentsDTO containerComponentsDTO);

}
