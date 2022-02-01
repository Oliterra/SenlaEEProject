package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface CourierControllerInterface {

    public ResponseEntity<String> getAllCouriers();

    public ResponseEntity<String> getCourier(@PathVariable("id") long id);

    public ResponseEntity<Void> updateCourier(@PathVariable long id, @RequestBody String updatedCourierJson);

    public ResponseEntity<Void> deleteCourier(@PathVariable("id") long id);

}
