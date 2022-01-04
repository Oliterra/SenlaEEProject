package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.TypeOfContainerControllerInterface;
import edu.senla.dto.TypeOfContainerDTO;
import edu.senla.dto.TypeOfContainerForUpdateDTO;
import edu.senla.exeptions.ConflictBetweenData;
import edu.senla.exeptions.NoContent;
import edu.senla.exeptions.NotFound;
import edu.senla.service.serviceinterface.TypeOfContainerServiceInterface;
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
@RequestMapping("/typesOfContainer")
public class TypeOfContainerController implements TypeOfContainerControllerInterface {

    private final TypeOfContainerServiceInterface typeOfContainerService;

    private final ObjectMapper mapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(TypeOfContainerController.class);

    @SneakyThrows
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getAllTypesOfContainer() {
        LOG.info("Getting all types of container");
        List<TypeOfContainerDTO> typeOfContainerDTOs = typeOfContainerService.getAllTypesOfContainer();
        if (typeOfContainerDTOs == null || typeOfContainerDTOs.isEmpty()){
            LOG.info("No types of container found");
            throw new NoContent();
        }
        String typeOfContainerJson = mapper.writeValueAsString(typeOfContainerDTOs);
        return new ResponseEntity<String>(typeOfContainerJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createTypeOfContainer(@RequestBody String typeOfContainerJson) {
        LOG.info("Creating new type of container: {}", typeOfContainerJson);
        TypeOfContainerDTO typeOfContainerDTO = mapper.readValue(typeOfContainerJson, TypeOfContainerDTO.class);
        if (typeOfContainerService.isTypeOfContainerExists(typeOfContainerDTO.getName(), typeOfContainerDTO.getNumberOfCalories())) {
            LOG.info("Type of container with name or caloric content already exists");
            throw new ConflictBetweenData();
        }
        typeOfContainerService.createTypeOfContainer(typeOfContainerDTO);
        LOG.info("Type of container with name {} successfully created", typeOfContainerDTO.getName());
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getTypeOfContainer(@PathVariable("id") long id) {
        LOG.info("Getting type of container with id: {}", id);
        if (!typeOfContainerService.isTypeOfContainerExists(id)) {
            LOG.info("There is no type of container with id {}", id);
            throw new NotFound();
        }
        TypeOfContainerDTO typeOfContainerDTO = typeOfContainerService.getTypeOfContainer(id);
        String typeOfContainerJson = mapper.writeValueAsString(typeOfContainerDTO);
        return new ResponseEntity<String>(typeOfContainerJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateTypeOfContainer(@PathVariable long id, @RequestBody String updatedTypeOfContainerJson) {
        LOG.info("Updating type of container with id {} with new data {}: ", id, updatedTypeOfContainerJson);
        TypeOfContainerForUpdateDTO updatedTypeOfContainerDTO = mapper.readValue(updatedTypeOfContainerJson, TypeOfContainerForUpdateDTO.class);
        if (!typeOfContainerService.isTypeOfContainerExists(id)) {
            LOG.info("There is no type of container with id {}", id);
            throw new NotFound();
        }
        if (typeOfContainerService.isTypeOfContainerExists(updatedTypeOfContainerDTO.getName())) {
            LOG.info("A container with name {} already exists", updatedTypeOfContainerDTO.getName());
            throw new ConflictBetweenData();
        }
        typeOfContainerService.updateTypeOfContainer(id, updatedTypeOfContainerDTO);
        LOG.info("Type of container with id {} successfully updated", id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTypeOfContainer(@PathVariable("id") long id) {
        LOG.info("Deleting type of container with id: {}", id);
        if (!typeOfContainerService.isTypeOfContainerExists(id)) {
            LOG.info("There is no type of container with id {}", id);
            throw new NotFound();
        }
        typeOfContainerService.deleteTypeOfContainer(id);
        LOG.info("Type of container with id {} successfully deleted", id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
