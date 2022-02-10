package edu.senla.controller.impl;

import edu.senla.controller.RegistrationController;
import edu.senla.service.ClientService;
import edu.senla.service.CourierService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping(value = "/clients")
    public void registerClient(@RequestBody String registrationRequestJson) {
        clientService.createClient(registrationRequestJson);
    }

    @PostMapping(value = "/couriers")
    public void registerCourier(@RequestBody String courierRegistrationRequestJson) {
        courierService.createCourier(courierRegistrationRequestJson);
    }
}
