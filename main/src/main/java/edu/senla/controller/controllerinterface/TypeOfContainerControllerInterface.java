package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface TypeOfContainerControllerInterface {

    public ResponseEntity<Void> createTypeOfContainer(@RequestBody String clientJson);

    public ResponseEntity<String> getTypeOfContainer(@PathVariable("id") int id);

    public ResponseEntity<Void> updateTypeOfContainer(@PathVariable int id, @RequestBody String updatedClientJson);

    public ResponseEntity<Void> deleteTypeOfContainer(@PathVariable("id") int id);

}
