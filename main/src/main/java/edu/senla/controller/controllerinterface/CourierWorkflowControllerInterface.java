package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;

public interface CourierWorkflowControllerInterface {

    public ResponseEntity<String> getCurrentOrderInfo();

    public ResponseEntity<String> getOrdersHistory();

    public ResponseEntity<Void> changeOwnStatus();

    public ResponseEntity<String> getNewOrder();

    public ResponseEntity<String> closeOrder();

}
