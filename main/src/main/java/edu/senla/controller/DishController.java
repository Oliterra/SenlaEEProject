package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.DishControllerInterface;
import edu.senla.dto.DishDTO;
import edu.senla.service.serviceinterface.DishServiceInterface;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dishes")
public class DishController implements DishControllerInterface {

    @Autowired
    private DishServiceInterface dishService;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(DishController.class);

    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createDish(String dishJson) {
        LOG.info("Creating new dish: {}", dishJson);
        DishDTO dishDTO = jacksonObjectMapper.readValue(dishJson, DishDTO.class);

        if (dishService.isDishExists(dishDTO)) {
            LOG.info("Dish with name " + dishDTO.getName() + " already exists");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        dishService.createDish(dishDTO);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getDish(int id) {
        LOG.info("Getting dish with id: {}", id);

        DishDTO dishDTO;
        try {
            dishDTO = dishService.readDish(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Dish with id {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>(jacksonObjectMapper.writeValueAsString(dishDTO), HttpStatus.OK);
    }

    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateDish(int id, String updatedDishJson) {
        LOG.info("Updating dish: ");

        try {
            DishDTO currentDish = dishService.readDish(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Dish with id {} not found", id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        DishDTO updatedDish = jacksonObjectMapper.readValue(updatedDishJson, DishDTO.class);

        dishService.updateDish(id, updatedDish);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteDish(int id) {
        LOG.info("Deleting dish with id: {}", id);

        try {
            DishDTO dishDTO = dishService.readDish(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Unable to delete. Dish with id {} not found", id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        dishService.deleteDish(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
