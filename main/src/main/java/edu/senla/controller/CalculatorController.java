package edu.senla.controller;

import edu.senla.model.dto.ContainerComponentsParamsDTO;

public interface CalculatorController {

    ContainerComponentsParamsDTO getWeightOfProductsInContainer(String containerComponentsJson);
}
