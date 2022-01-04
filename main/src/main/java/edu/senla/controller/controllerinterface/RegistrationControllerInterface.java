package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface RegistrationControllerInterface {

    public ResponseEntity<Void> registerClient(@RequestBody String registrationRequestJson);

    public ResponseEntity<Void> registerCourier(@RequestBody String courierRegistrationRequestJson);

}
