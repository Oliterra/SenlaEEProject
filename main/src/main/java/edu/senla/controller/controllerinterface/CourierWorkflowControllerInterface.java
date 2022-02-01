package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;

public interface CourierWorkflowControllerInterface {

    public ResponseEntity<String> getCurrentOrderInfo();

    public ResponseEntity<Void> changeStatus();

    public ResponseEntity<String> getNewOrder();


}
