package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.CourierControllerInterface;
import edu.senla.dto.CourierDTO;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/couriers")
public class CourierController implements CourierControllerInterface {

    @Autowired
    private CourierServiceInterface courierService;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(CourierController.class);

    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createCourier(@RequestBody String courierJson) {
        LOG.info("Creating new courier: {}", courierJson);
        CourierDTO courierDTO = jacksonObjectMapper.readValue(courierJson, CourierDTO.class);

        if (courierService.isCourierExists(courierDTO)) {
            LOG.info("Courier with phone " + courierDTO.getPhone() + " already exists");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        courierService.createCourier(courierDTO);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getCourier(@PathVariable("id") int id) {
        LOG.info("Getting courier with id: {}", id);

        CourierDTO courierDTO;
        try {
            courierDTO = courierService.readCourier(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Courier with id {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>(jacksonObjectMapper.writeValueAsString(courierDTO), HttpStatus.OK);
    }

    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateCourier(@PathVariable int id, @RequestBody String updatedCourierJson) {
        LOG.info("Updating courier: ");

        try {
            CourierDTO currentCourier = courierService.readCourier(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Courier with id {} not found", id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        CourierDTO updatedCourier = jacksonObjectMapper.readValue(updatedCourierJson, CourierDTO.class);

        courierService.updateCourier(id, updatedCourier);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCourier(@PathVariable("id") int id) {
        LOG.info("Deleting courier with id: {}", id);

        try {
            CourierDTO courierDTO = courierService.readCourier(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Unable to delete. Courier with id {} not found", id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        courierService.deleteCourier(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
