package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationControllerInterface {

    public ResponseEntity<String> authenticateClient(@RequestBody String authRequestJson);

    public ResponseEntity<String> authenticateCourier(@RequestBody String authRequestCourierJson);

}
