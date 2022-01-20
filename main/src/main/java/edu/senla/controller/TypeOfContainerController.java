package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.TypeOfContainerControllerInterface;
import edu.senla.dto.TypeOfContainerDTO;
import edu.senla.dto.TypeOfContainerForUpdateDTO;
import edu.senla.service.serviceinterface.TypeOfContainerServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/typesOfContainer")
public class TypeOfContainerController implements TypeOfContainerControllerInterface {

    private final TypeOfContainerServiceInterface typeOfContainerService;

    private final ObjectMapper mapper;

    @SneakyThrows
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getAllTypesOfContainer() {
        String typeOfContainerJson = mapper.writeValueAsString(typeOfContainerService.getAllTypesOfContainer());
        return new ResponseEntity<String>(typeOfContainerJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createTypeOfContainer(@RequestBody String typeOfContainerJson) {
        typeOfContainerService.createTypeOfContainer( mapper.readValue(typeOfContainerJson, TypeOfContainerDTO.class));
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getTypeOfContainer(@PathVariable("id") long id) {
        String typeOfContainerJson = mapper.writeValueAsString(typeOfContainerService.getTypeOfContainer(id));
        return new ResponseEntity<String>(typeOfContainerJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateTypeOfContainer(@PathVariable long id, @RequestBody String updatedTypeOfContainerJson) {
        typeOfContainerService.updateTypeOfContainer(id, mapper.readValue(updatedTypeOfContainerJson, TypeOfContainerForUpdateDTO.class));
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTypeOfContainer(@PathVariable("id") long id) {
        typeOfContainerService.deleteTypeOfContainer(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
