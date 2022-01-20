package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.CalculatorControllerInterface;
import edu.senla.dto.ContainerComponentsDTO;
import edu.senla.dto.ContainerComponentsParamsDTO;
import edu.senla.service.serviceinterface.ContainerServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculations")
@RequiredArgsConstructor
public class CalculatorController implements CalculatorControllerInterface {

    private final ContainerServiceInterface containerService;

    private final ObjectMapper mapper;

    @SneakyThrows
    @Secured({"ROLE_USER"})
    @RequestMapping(value = "/containerParams", method = RequestMethod.GET)
    public ResponseEntity<String> getWeightOfProductsInContainer(@RequestBody String containerComponentsJson) {
        ContainerComponentsDTO containerComponentsDTO = mapper.readValue(containerComponentsJson, ContainerComponentsDTO.class);
        ContainerComponentsParamsDTO containerComponentsParamsDTO = containerService.calculateWeightOfDishes(containerComponentsDTO);
        String containerComponentsWeightJson = mapper.writeValueAsString(containerComponentsParamsDTO);
        return new ResponseEntity<String>(containerComponentsWeightJson, HttpStatus.OK);
    }

}
