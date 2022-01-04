package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.DishControllerInterface;
import edu.senla.dto.DishDTO;
import edu.senla.exeptions.BadRequest;
import edu.senla.exeptions.ConflictBetweenData;
import edu.senla.exeptions.NoContent;
import edu.senla.exeptions.NotFound;
import edu.senla.service.serviceinterface.DishServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dishes")
public class DishController implements DishControllerInterface {

    private final DishServiceInterface dishService;

    private final ObjectMapper mapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(DishController.class);

    @SneakyThrows
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getAllDishes() {
        LOG.info("Getting all dishes");
        List<DishDTO> dishDTOs = dishService.getAllDishes();
        if (dishDTOs == null || dishDTOs.isEmpty()){
            LOG.info("No dishes  found");
            throw new NoContent();
        }
        String dishJson = mapper.writeValueAsString(dishDTOs);
        return new ResponseEntity<String>(dishJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createDish(@RequestBody String dishJson) {
        LOG.info("Creating new dish: {}", dishJson);
        DishDTO dishDTO = mapper.readValue(dishJson, DishDTO.class);
        if (dishService.isDishExists(dishDTO.getName())) {
            LOG.info("Dish with name {} already exists", dishDTO.getName());
            throw new ConflictBetweenData();
        }
        if (!dishService.isDishTypeCorrect(dishDTO.getDishType())) {
            LOG.info("Dish type {} invalid", dishDTO.getDishType());
            throw new BadRequest();
        }
        dishService.createDish(dishDTO);
        LOG.info("Dish with name {} successfully created", dishDTO.getName());
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getDish(@PathVariable("id") long id) {
        LOG.info("Getting dish with id: {}", id);
        if (!dishService.isDishExists(id)) {
            LOG.info("There is no dish with id {}", id);
            throw new NotFound();
        }
        DishDTO dishDTO = dishService.getDish(id);
        String dishJson = mapper.writeValueAsString(dishDTO);
        return new ResponseEntity<String>(dishJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateDish(@PathVariable("id") long id, @RequestBody String updatedDishJson) {
        LOG.info("Updating dish with id {} with new data {}: ", id, updatedDishJson);
        DishDTO updatedDishDTO = mapper.readValue(updatedDishJson, DishDTO.class);
        if (!dishService.isDishExists(id)) {
            LOG.info("There is no dish with id {}", id);
            throw new NotFound();
        }
        if (!dishService.isDishTypeCorrect(updatedDishDTO.getDishType())) {
            LOG.info("Dish type {} invalid", updatedDishDTO.getDishType());
            throw new BadRequest();
        }
        dishService.updateDish(id, updatedDishDTO);
        LOG.info("Dish with id {} successfully updated", id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteDish(@PathVariable("id") long id) {
        LOG.info("Deleting dish with id: {}", id);
        if (!dishService.isDishExists(id)) {
            LOG.info("There is no dish with id {}", id);
            throw new NotFound();
        }
        dishService.deleteDish(id);
        LOG.info("Dish with id {} successfully deleted", id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
