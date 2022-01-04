package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface TypeOfContainerControllerInterface {

    public ResponseEntity<String> getAllTypesOfContainer();

    public ResponseEntity<Void> createTypeOfContainer(@RequestBody String typeOfContainerJson);

    public ResponseEntity<String> getTypeOfContainer(@PathVariable("id") long id);

    public ResponseEntity<Void> updateTypeOfContainer(@PathVariable long id, @RequestBody String updatedTypeOfContainerJson);

    public ResponseEntity<Void> deleteTypeOfContainer(@PathVariable("id") long id);

}
