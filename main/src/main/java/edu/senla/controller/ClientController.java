package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.ClientControllerInterface;
import edu.senla.dto.ClientMainInfoDTO;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController implements ClientControllerInterface {

    private final ClientServiceInterface clientService;

    private final ObjectMapper mapper;

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getAllClients() {
        String clientsMainInfoJson = mapper.writeValueAsString(clientService.getAllClients());
        return new ResponseEntity<String>(clientsMainInfoJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getClient(@PathVariable("id") long id) {
        String clientMainInfoJson = mapper.writeValueAsString(clientService.getClient(id));
        return new ResponseEntity<String>(clientMainInfoJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateClient(@PathVariable long id, @RequestBody String updatedClientJson) {
        clientService.updateClient(id, mapper.readValue(updatedClientJson, ClientMainInfoDTO.class));
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteClient(@PathVariable("id") long id) {
        clientService.deleteClient(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}

