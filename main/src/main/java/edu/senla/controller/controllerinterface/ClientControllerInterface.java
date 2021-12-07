package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface ClientControllerInterface {

    public ResponseEntity<Void> createClient(@RequestBody String clientJson);

    public ResponseEntity<String> getClient(@PathVariable("id") int id);

    public ResponseEntity<Void> updateClient(@PathVariable int id, @RequestBody String updatedClientJson);

    public ResponseEntity<Void> deleteClient(@PathVariable("id") int id);

}

