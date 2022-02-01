package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface ClientControllerInterface {

    public ResponseEntity<String> getAllClients();

    public ResponseEntity<String> getClient(@PathVariable("id") long id);

    public ResponseEntity<Void> updateClient(@PathVariable long id, @RequestBody String updatedClientJson);

    public ResponseEntity<Void> deleteClient(@PathVariable("id") long id);

}

