package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderControllerInterface {

    public ResponseEntity<Void> createOrder(@RequestBody String orderJson);

    public ResponseEntity<String> getOrder(@PathVariable("id") int id);

    public ResponseEntity<Void> updateOrder(@PathVariable int id, @RequestBody String updatedOrderJson);

    public ResponseEntity<Void> deleteOrder(@PathVariable("id") int id);

}
