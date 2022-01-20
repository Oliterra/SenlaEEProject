package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.RegistrationControllerInterface;
import edu.senla.dto.CourierRegistrationRequestDTO;
import edu.senla.dto.RegistrationRequestDTO;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController implements RegistrationControllerInterface {

    private final ClientServiceInterface clientService;

    private final CourierServiceInterface courierService;

    private final ObjectMapper jacksonObjectMapper;

    @SneakyThrows
    @RequestMapping(value = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Void> registerClient(@RequestBody String registrationRequestJson) {
        clientService.createClient(jacksonObjectMapper.readValue(registrationRequestJson, RegistrationRequestDTO.class));
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @SneakyThrows
    @RequestMapping(value = "/couriers", method = RequestMethod.POST)
    public ResponseEntity<Void> registerCourier(@RequestBody String courierRegistrationRequestJson) {
        courierService.createCourier(jacksonObjectMapper.readValue(courierRegistrationRequestJson, CourierRegistrationRequestDTO.class));
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

}
