package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.CourierControllerInterface;
import edu.senla.dto.CourierMainInfoDTO;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/couriers")
public class CourierController implements CourierControllerInterface {

    private final CourierServiceInterface courierService;

    private final ObjectMapper mapper;

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getAllCouriers() {
        String courierMainInfoJson = mapper.writeValueAsString(courierService.getAllCouriers());
        return new ResponseEntity<String>(courierMainInfoJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getCourier(@PathVariable("id") long id) {
        String courierMainInfoJson = mapper.writeValueAsString(courierService.getCourier(id));
        return new ResponseEntity<String>(courierMainInfoJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateCourier(@PathVariable long id, @RequestBody String updatedCourierJson) {
        courierService.updateCourier(id, mapper.readValue(updatedCourierJson, CourierMainInfoDTO.class));
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCourier(@PathVariable("id") long id) {
        courierService.deleteCourier(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
