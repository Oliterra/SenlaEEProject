package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface OrdersHistoryControllerInterface {

    public ResponseEntity<String> getClientOrdersHistory(@PathVariable long id);

    public ResponseEntity<String> getCouriersOrdersHistory(@PathVariable long id);

}
