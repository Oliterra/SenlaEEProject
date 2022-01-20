package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface OrderConfirmationControllerInterface {

    public ResponseEntity<Void> confirmReceiptOfTheOrderClient(@PathVariable long orderId);

    public ResponseEntity<String> closeOrderCourier();

}
