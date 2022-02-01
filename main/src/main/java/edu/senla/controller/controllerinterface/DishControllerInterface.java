package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface DishControllerInterface {

    public ResponseEntity<String> getAllDishes();

    public ResponseEntity<Void> createDish(@RequestBody String dishJson);

    public ResponseEntity<String> getDish(@PathVariable("id") long id);

    public ResponseEntity<Void> updateDish(@PathVariable("id") long id, @RequestBody String updatedDishJson);

    public ResponseEntity<Void> deleteDish(@PathVariable("id") long id);

}
