package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface OrderControllerInterface {

    public ResponseEntity<String> getAllOrders();

    public ResponseEntity<String> getOrder(@PathVariable("id") long id);

    public ResponseEntity<Void> deleteOrder(@PathVariable("id") long id);

}
