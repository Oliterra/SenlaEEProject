package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.dto.ClientDTO;
import edu.senla.dto.RegistrationRequestDTO;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private ClientServiceInterface clientService;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(RegistrationController.class);

    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> registerClient(@RequestBody String clientParamsJson, String registrationRequestJson) {
        ClientDTO clientDTO = jacksonObjectMapper.readValue(clientParamsJson, ClientDTO.class);
        RegistrationRequestDTO registrationRequestDTO = jacksonObjectMapper.readValue(registrationRequestJson, RegistrationRequestDTO.class);

        clientDTO.setPassword(registrationRequestDTO.getPassword());
        clientDTO.setUsername(registrationRequestDTO.getUsername());

        if (clientService.isClientExists(clientDTO)) {
            LOG.info("User with email " + clientDTO.getEmail() + " already exists");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        clientService.createClient(clientDTO);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

}
