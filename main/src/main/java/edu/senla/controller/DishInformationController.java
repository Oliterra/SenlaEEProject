package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.DishInformationControllerInterface;
import edu.senla.dto.DishDTO;
import edu.senla.dto.DishInformationDTO;
import edu.senla.service.serviceinterface.DishInformationServiceInterface;
import edu.senla.service.serviceinterface.DishServiceInterface;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dishesInformation")
public class DishInformationController implements DishInformationControllerInterface {

    @Autowired
    private DishInformationServiceInterface dishInformationService;

    @Autowired
    private DishServiceInterface dishService;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(DishInformationController.class);

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createDishInformation(@RequestBody String dishInformationJson) {
        LOG.info("Creating new dish information: {}", dishInformationJson);
        DishInformationDTO dishInformationDTO = jacksonObjectMapper.readValue(dishInformationJson, DishInformationDTO.class);
        int dishId = dishInformationDTO.getDishId();

        try {
            DishDTO dishDTO = dishService.readDish(dishId);
        } catch (IllegalArgumentException exception) {
            LOG.info("Dish for which the information is being created with id {} not found", dishId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        dishInformationService.createDishInformation(dishId, dishInformationDTO);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getDishInformation(@PathVariable("id") int id) {
        LOG.info("Getting dish information with id: {}", id);

        DishInformationDTO dishInformationDTO;
        try {
            dishInformationDTO = dishInformationService.readDishInformation(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Dish information with id {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>(jacksonObjectMapper.writeValueAsString(dishInformationDTO), HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateDishInformation(@PathVariable int id, @RequestBody String updatedDishInformationJson) {
        LOG.info("Updating dish information: ");

        try {
            DishInformationDTO currentDishInformation = dishInformationService.readDishInformation(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Dish information with id {} not found", id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        DishInformationDTO updatedDishInformation = jacksonObjectMapper.readValue(updatedDishInformationJson, DishInformationDTO.class);

        dishInformationService.updateDishInformation(id, updatedDishInformation);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteDishInformation(@PathVariable("id") int id) {
        LOG.info("Deleting dish information with id: {}", id);

        try {
            DishInformationDTO dishInformationDTO = dishInformationService.readDishInformation(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Unable to delete. Dish information with id {} not found", id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        dishInformationService.deleteDishInformation(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
