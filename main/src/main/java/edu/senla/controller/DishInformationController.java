package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.DishInformationControllerInterface;
import edu.senla.dto.DishInformationDTO;
import edu.senla.dto.DishInformationForUpdateDTO;
import edu.senla.service.serviceinterface.DishInformationServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dishesInformation")
public class DishInformationController implements DishInformationControllerInterface {

    private final DishInformationServiceInterface dishInformationService;

    private final ObjectMapper mapper;
    
    @SneakyThrows
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getAllDishesInformation() {
        String dishInformationJson = mapper.writeValueAsString(dishInformationService.getAllDishesInformation());
        return new ResponseEntity<String>(dishInformationJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createDishInformation(@RequestBody String dishInformationJson) {
        dishInformationService.createDishInformation(mapper.readValue(dishInformationJson, DishInformationDTO.class));
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getDishInformation(@PathVariable("id") long id) {
        String dishInformationJson = mapper.writeValueAsString(dishInformationService.getDishInformation(id));
        return new ResponseEntity<String>(dishInformationJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateDishInformation(@PathVariable long id, @RequestBody String updatedDishInformationJson) {
        dishInformationService.updateDishInformation(id, mapper.readValue(updatedDishInformationJson, DishInformationForUpdateDTO.class));
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteDishInformation(@PathVariable("id") int id) {
        dishInformationService.deleteDishInformation(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
