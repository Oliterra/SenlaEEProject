package edu.senla.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.TypeOfContainerController;
import edu.senla.model.dto.TypeOfContainerDTO;
import edu.senla.model.dto.TypeOfContainerForUpdateDTO;
import edu.senla.service.TypeOfContainerService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/typesOfContainer")
public class TypeOfContainerControllerImpl implements TypeOfContainerController {

    private final TypeOfContainerService typeOfContainerService;
    private final ObjectMapper mapper;

    @GetMapping
    public List<TypeOfContainerDTO> getAllTypesOfContainer(@RequestParam(value = "pages", required = false, defaultValue = "10") int pages) {
        return typeOfContainerService.getAllTypesOfContainer(pages);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @PostMapping
    public void createTypeOfContainer(@RequestBody String typeOfContainerJson) {
        typeOfContainerService.createTypeOfContainer(mapper.readValue(typeOfContainerJson, TypeOfContainerDTO.class));
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @GetMapping(value = "{id}")
    public TypeOfContainerDTO getTypeOfContainer(@PathVariable("id") long id) {
        return typeOfContainerService.getTypeOfContainer(id);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @PutMapping(value = "{id}")
    public void updateTypeOfContainer(@PathVariable long id, @RequestBody String updatedTypeOfContainerJson) {
        typeOfContainerService.updateTypeOfContainer(id, mapper.readValue(updatedTypeOfContainerJson, TypeOfContainerForUpdateDTO.class));
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "{id}")
    public void deleteTypeOfContainer(@PathVariable("id") long id) {
        typeOfContainerService.deleteTypeOfContainer(id);
    }
}
