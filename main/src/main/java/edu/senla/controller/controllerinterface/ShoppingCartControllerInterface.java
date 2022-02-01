package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface ShoppingCartControllerInterface {

    public ResponseEntity<String> makeOrder(@RequestBody String shoppingCartJson);

}
