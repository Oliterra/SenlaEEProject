package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.TypeOfContainerControllerInterface;
import edu.senla.dto.TypeOfContainerDTO;
import edu.senla.service.serviceinterface.TypeOfContainerServiceInterface;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/typesOfContainer")
public class TypeOfContainerController implements TypeOfContainerControllerInterface {

    @Autowired
    private TypeOfContainerServiceInterface typeOfContainerService;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(TypeOfContainerController.class);

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createTypeOfContainer(@RequestBody String typeOfContainerJson) {
        LOG.info("Creating new type of container: {}", typeOfContainerJson);
        TypeOfContainerDTO typeOfContainerDTO = jacksonObjectMapper.readValue(typeOfContainerJson, TypeOfContainerDTO.class);

        if (typeOfContainerService.isTypeOfContainerExists(typeOfContainerDTO)) {
            LOG.info("Type of container with caloric content " + typeOfContainerDTO.getNumberOfCalories() + " already exists");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        typeOfContainerService.createTypeOfContainer(typeOfContainerDTO);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getTypeOfContainer(@PathVariable("id") int id) {
        LOG.info("Getting type of container with id: {}", id);

        TypeOfContainerDTO typeOfContainerDTO;
        try {
            typeOfContainerDTO = typeOfContainerService.readTypeOfContainer(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Type of container with id {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>(jacksonObjectMapper.writeValueAsString(typeOfContainerDTO), HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateTypeOfContainer(@PathVariable int id, @RequestBody String updatedTypeOfContainerJson) {
        LOG.info("Updating type of container: ");

        try {
            TypeOfContainerDTO currentTypeOfContainer = typeOfContainerService.readTypeOfContainer(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Type of container with id {} not found", id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        TypeOfContainerDTO updatedTypeOfContainer = jacksonObjectMapper.readValue(updatedTypeOfContainerJson, TypeOfContainerDTO.class);

        typeOfContainerService.updateTypeOfContainer(id, updatedTypeOfContainer);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTypeOfContainer(@PathVariable("id") int id) {
        LOG.info("Deleting type of container with id: {}", id);

        try {
            TypeOfContainerDTO typeOfContainerDTO = typeOfContainerService.readTypeOfContainer(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Unable to delete. Type of container with id {} not found", id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        typeOfContainerService.deleteTypeOfContainer(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
