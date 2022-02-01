package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface CalculatorControllerInterface {

    public ResponseEntity<String> getWeightOfProductsInContainer(@RequestBody String containerComponentsJson);

}
