package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface DishControllerInterface {

    public ResponseEntity<Void> createDish(@RequestBody String dishJson);

    public ResponseEntity<String> getDish(@PathVariable("id") int id);

    public ResponseEntity<Void> updateDish(@PathVariable int id, @RequestBody String updatedDishJson);

    public ResponseEntity<Void> deleteDish(@PathVariable("id") int id);

}
