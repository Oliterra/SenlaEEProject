package edu.senla.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.RegistrationController;
import edu.senla.model.dto.CourierRegistrationRequestDTO;
import edu.senla.model.dto.RegistrationRequestDTO;
import edu.senla.service.ClientService;
import edu.senla.service.CourierService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationControllerImpl implements RegistrationController {

    private final ClientService clientService;
    private final CourierService courierService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @PostMapping(value = "/clients")
    public void registerClient(@RequestBody String registrationRequestJson) {
        clientService.createClient(objectMapper.readValue(registrationRequestJson, RegistrationRequestDTO.class));
    }

    @SneakyThrows
    @PostMapping(value = "/couriers")
    public void registerCourier(@RequestBody String courierRegistrationRequestJson) {
        courierService.createCourier(objectMapper.readValue(courierRegistrationRequestJson, CourierRegistrationRequestDTO.class));
    }
}
