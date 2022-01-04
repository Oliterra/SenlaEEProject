package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface ShoppingCartControllerInterface {

    public ResponseEntity<String> getOrdersHistory();

    public ResponseEntity<String> getWeightOfProductsInContainer(@RequestBody String containerComponentsJson);

    public ResponseEntity<String> makeOrder(@RequestBody String shoppingCartJson);

    public ResponseEntity<Void> confirmReceiptOfTheOrder(@PathVariable long id);

}
