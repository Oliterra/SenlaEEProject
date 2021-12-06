package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface CourierControllerInterface {

    public ResponseEntity<Void> createCourier(@RequestBody String courierJson);

    public ResponseEntity<String> getCourier(@PathVariable("id") int id);

    public ResponseEntity<Void> updateCourier(@PathVariable int id, @RequestBody String updatedCourierJson);

    public ResponseEntity<Void> deleteCourier(@PathVariable("id") int id);
}
