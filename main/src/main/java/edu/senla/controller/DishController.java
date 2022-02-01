package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.DishControllerInterface;
import edu.senla.dto.DishDTO;
import edu.senla.service.serviceinterface.DishServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dishes")
public class DishController implements DishControllerInterface {

    private final DishServiceInterface dishService;

    private final ObjectMapper mapper;

    @SneakyThrows
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getAllDishes() {
        String dishJson = mapper.writeValueAsString(dishService.getAllDishes());
        return new ResponseEntity<String>(dishJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createDish(@RequestBody String dishJson) {
        dishService.createDish(mapper.readValue(dishJson, DishDTO.class));
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getDish(@PathVariable("id") long id) {
        String dishJson = mapper.writeValueAsString(dishService.getDish(id));
        return new ResponseEntity<String>(dishJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateDish(@PathVariable("id") long id, @RequestBody String updatedDishJson) {
        dishService.updateDish(id, mapper.readValue(updatedDishJson, DishDTO.class));
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteDish(@PathVariable("id") long id) {
        dishService.deleteDish(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
