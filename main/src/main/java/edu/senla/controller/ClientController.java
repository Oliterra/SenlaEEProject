package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.ClientControllerInterface;
import edu.senla.dto.ClientDTO;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientController implements ClientControllerInterface {

    @Autowired
    private ClientServiceInterface clientService;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(ClientController.class);

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createClient(@RequestBody String clientJson) {
        LOG.info("Creating new client: {}", clientJson);
        ClientDTO clientDTO = jacksonObjectMapper.readValue(clientJson, ClientDTO.class);

        if (clientService.isClientExists(clientDTO)) {
            LOG.info("Client with email " + clientDTO.getEmail() + " already exists");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        clientService.createClient(clientDTO);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getClient(@PathVariable("id") int id) {
        LOG.info("Getting client with id: {}", id);

        ClientDTO clientDTO;
        try {
            clientDTO = clientService.readClient(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Client with id {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>(jacksonObjectMapper.writeValueAsString(clientDTO), HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateClient(@PathVariable int id, @RequestBody String updatedClientJson) {
        LOG.info("Updating client: ");

        try {
            ClientDTO currentClient = clientService.readClient(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Client with id {} not found", id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        ClientDTO updatedClient = jacksonObjectMapper.readValue(updatedClientJson, ClientDTO.class);

        clientService.updateClient(id, updatedClient);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteClient(@PathVariable("id") int id) {
        LOG.info("Deleting client with id: {}", id);

        try {
            ClientDTO clientDTO = clientService.readClient(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Unable to delete. Client with id {} not found", id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        clientService.deleteClient(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}

