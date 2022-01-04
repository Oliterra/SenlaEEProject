package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.ClientControllerInterface;
import edu.senla.dto.ClientMainInfoDTO;
import edu.senla.exeptions.ConflictBetweenData;
import edu.senla.exeptions.NoContent;
import edu.senla.exeptions.NotFound;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController implements ClientControllerInterface {

    private final ClientServiceInterface clientService;

    private final ObjectMapper mapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(ClientController.class);

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getAllClients() {
        LOG.info("Getting all clients");
        List<ClientMainInfoDTO> clientMainInfoDTOs = clientService.getAllClients();
        if (clientMainInfoDTOs == null || clientMainInfoDTOs.isEmpty()){
            LOG.info("No clients found");
            throw new NoContent();
        }
        String clientsMainInfoJson = mapper.writeValueAsString(clientMainInfoDTOs);
        return new ResponseEntity<String>(clientsMainInfoJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getClient(@PathVariable("id") long id) {
        LOG.info("Getting client with id: {}", id);
        if (!clientService.isClientExists(id)) {
            LOG.info("There is no client with id {}", id);
            throw new NotFound();
        }
        ClientMainInfoDTO clientMainInfoDTO = clientService.getClient(id);
        String clientMainInfoJson = mapper.writeValueAsString(clientMainInfoDTO);
        return new ResponseEntity<String>(clientMainInfoJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateClient(@PathVariable long id, @RequestBody String updatedClientJson) {
        LOG.info("Updating client with id {} with new data {}: ", id, updatedClientJson);
        ClientMainInfoDTO updatedClientMainInfoDTO = mapper.readValue(updatedClientJson, ClientMainInfoDTO.class);
        if (!clientService.isClientExists(id)) {
            LOG.info("There is no client with id {}", id);
            throw new NotFound();
        }
        String possibleDuplicate = clientService.isClientExists(updatedClientMainInfoDTO);
        if (possibleDuplicate != null) {
            LOG.info("A user with this {} already exists ", possibleDuplicate);
            throw new ConflictBetweenData();
        }
        clientService.updateClient(id, updatedClientMainInfoDTO);
        LOG.info("Client with id {} successfully updated", id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteClient(@PathVariable("id") long id) {
        LOG.info("Deleting client with id: {}", id);
        if (!clientService.isClientExists(id)) {
            LOG.info("There is no client with id {}", id);
            throw new NotFound();
        }
        clientService.deleteClient(id);
        LOG.info("Client with id {} successfully deleted", id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}

