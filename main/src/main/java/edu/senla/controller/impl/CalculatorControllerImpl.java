package edu.senla.controller.impl;

import edu.senla.controller.CalculatorController;
import edu.senla.model.dto.ContainerComponentsParamsDTO;
import edu.senla.service.ContainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculations")
@RequiredArgsConstructor
public class CalculatorControllerImpl implements CalculatorController {

    private final ContainerService containerService;

    @Secured({"ROLE_USER"})
    @GetMapping(value = "/containerParams")
    public ContainerComponentsParamsDTO getWeightOfProductsInContainer(@RequestBody String containerComponentsJson) {
        return containerService.calculateWeightOfDishes(containerComponentsJson);
    }
}
