package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.DishInformationControllerInterface;
import edu.senla.dto.DishInformationDTO;
import edu.senla.exeptions.ConflictBetweenData;
import edu.senla.exeptions.NoContent;
import edu.senla.exeptions.NotFound;
import edu.senla.service.serviceinterface.DishInformationServiceInterface;
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
@RequestMapping("/dishesInformation")
public class DishInformationController implements DishInformationControllerInterface {

    private final DishInformationServiceInterface dishInformationService;

    private final DishServiceInterface dishService;

    private final ObjectMapper mapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(DishInformationController.class);
    
    @SneakyThrows
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getAllDishesInformation() {
        LOG.info("Getting all dishes information");
        List<DishInformationDTO> dishInformationDTOs = dishInformationService.getAllDishesInformation();
        if (dishInformationDTOs == null || dishInformationDTOs.isEmpty()){
            LOG.info("No dishes information found");
            throw new NoContent();
        }
        String dishInformationJson = mapper.writeValueAsString(dishInformationDTOs);
        return new ResponseEntity<String>(dishInformationJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createDishInformation(@RequestBody String dishInformationJson) {
        LOG.info("Creating new dish information: {}", dishInformationJson);
        DishInformationDTO dishInformationDTO = mapper.readValue(dishInformationJson, DishInformationDTO.class);
        if (!dishService.isDishExists(dishInformationDTO.getDishId())) {
            LOG.info("Dish for which the information is being created with id {} not found", dishInformationDTO.getDishId());
            throw new NotFound();
        }
        if (dishService.isDishHasDishInformation(dishInformationDTO.getDishId())) {
            LOG.info("Dish information for dish with id {} is already exists", dishInformationDTO.getDishId());
            throw new ConflictBetweenData();
        }
        dishInformationService.createDishInformation(dishInformationDTO);
        LOG.info("Dish information for dish with id {} successfully created", dishInformationDTO.getDishId());
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getDishInformation(@PathVariable("id") long id) {
        LOG.info("Getting dish information with id: {}", id);
        if (!dishInformationService.isDishInformationExists(id)) {
            LOG.info("There is no dish information with id {}", id);
            throw new NotFound();
        }
        DishInformationDTO dishInformationDTO = dishInformationService.getDishInformation(id);
        String dishInformationJson = mapper.writeValueAsString(dishInformationDTO);
        return new ResponseEntity<String>(dishInformationJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateDishInformation(@PathVariable long id, @RequestBody String updatedDishInformationJson) {
        LOG.info("Updating dish information with id {} with new data {}: ", id, updatedDishInformationJson);
        DishInformationDTO updatedDishInformationDTO = mapper.readValue(updatedDishInformationJson, DishInformationDTO.class);
        if (!dishInformationService.isDishInformationExists(id)) {
            LOG.info("There is no dish information with id {}", id);
            throw new NotFound();
        }
        dishInformationService.updateDishInformation(id, updatedDishInformationDTO);
        LOG.info("Dish information with id {} successfully updated", id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteDishInformation(@PathVariable("id") int id) {
        LOG.info("Deleting dish information with id: {}", id);
        if (!dishInformationService.isDishInformationExists(id)) {
            LOG.info("There is no dish information with id {}", id);
            throw new NotFound();
        }
        dishInformationService.deleteDishInformation(id);
        LOG.info("Dish information with id {} successfully deleted", id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
