package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface DishInformationControllerInterface {

    public ResponseEntity<String> getAllDishesInformation();

    public ResponseEntity<Void> createDishInformation(@RequestBody String dishInformationJson);

    public ResponseEntity<String> getDishInformation(@PathVariable("id") long id);

    public ResponseEntity<Void> updateDishInformation(@PathVariable long id, @RequestBody String updatedDishInformationJson);

    public ResponseEntity<Void> deleteDishInformation(@PathVariable("id") int id);

}
