package edu.senla.controller;

import edu.senla.dto.ClientDTO;
import edu.senla.dto.RegistrationRequestDTO;
import edu.senla.service.serviceinterface.ClientServiceInterface;
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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> registerClient(@RequestBody RegistrationRequestDTO registrationRequestDTO) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setPassword(registrationRequestDTO.getPassword());
        clientDTO.setUsername(registrationRequestDTO.getUsername());
        clientService.createClient(clientDTO);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

}
