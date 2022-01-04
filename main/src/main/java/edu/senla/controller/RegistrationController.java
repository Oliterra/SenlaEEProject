package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.RegistrationControllerInterface;
import edu.senla.dto.CourierRegistrationRequestDTO;
import edu.senla.dto.RegistrationRequestDTO;
import edu.senla.exeptions.BadRequest;
import edu.senla.exeptions.ConflictBetweenData;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
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

    private final Logger LOG = (Logger) LoggerFactory.getLogger(RegistrationController.class);

    @SneakyThrows
    @RequestMapping(value = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Void> registerClient(@RequestBody String registrationRequestJson) {
        RegistrationRequestDTO registrationRequestDTO = jacksonObjectMapper.readValue(registrationRequestJson, RegistrationRequestDTO.class);
        String possibleDuplicate = clientService.isClientExists(registrationRequestDTO);
        if (possibleDuplicate != null) {
            LOG.info("A user with this {} already exists ", possibleDuplicate);
            throw new ConflictBetweenData();
        }
        if (!(registrationRequestDTO.getPassword()).equals(registrationRequestDTO.getPasswordConfirm())) {
            LOG.info("Passwords {} and {} do not match", registrationRequestDTO.getPassword(), registrationRequestDTO.getPasswordConfirm());
            throw new BadRequest();
        }
        clientService.createClient(registrationRequestDTO);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @SneakyThrows
    @RequestMapping(value = "/couriers", method = RequestMethod.POST)
    public ResponseEntity<Void> registerCourier(@RequestBody String courierRegistrationRequestJson) {
        if (courierRegistrationRequestJson == null) {
            LOG.info("Incorrect data");
            throw new BadRequest();
        }
        CourierRegistrationRequestDTO courierRegistrationRequestDTO = jacksonObjectMapper.readValue(courierRegistrationRequestJson, CourierRegistrationRequestDTO.class);
        String couriersPhone = courierRegistrationRequestDTO.getPhone();
        if (courierService.isCourierExists(couriersPhone)) {
            LOG.info("A courier with phone {} already exists ", couriersPhone);
            throw new ConflictBetweenData();
        }
        if (!(courierRegistrationRequestDTO.getPassword()).equals(courierRegistrationRequestDTO.getPasswordConfirm())) {
            LOG.info("Passwords {} and {} do not match", courierRegistrationRequestDTO.getPassword(), courierRegistrationRequestDTO.getPasswordConfirm());
            throw new BadRequest();
        }
        courierService.createCourier(courierRegistrationRequestDTO);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

}
