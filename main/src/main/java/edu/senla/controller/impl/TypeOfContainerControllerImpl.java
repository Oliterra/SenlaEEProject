package edu.senla.controller.impl;

import edu.senla.controller.TypeOfContainerController;
import edu.senla.model.dto.TypeOfContainerDTO;
import edu.senla.service.TypeOfContainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/typesOfContainer")
public class TypeOfContainerControllerImpl implements TypeOfContainerController {

    private final TypeOfContainerService typeOfContainerService;

    @GetMapping
    public List<TypeOfContainerDTO> getAllTypesOfContainer(@RequestParam(value = "pages", required = false, defaultValue = "10") int pages) {
        return typeOfContainerService.getAllTypesOfContainer(pages);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public void createTypeOfContainer(@RequestBody String typeOfContainerJson) {
        typeOfContainerService.createTypeOfContainer(typeOfContainerJson);
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "{id}")
    public TypeOfContainerDTO getTypeOfContainer(@PathVariable("id") long id) {
        return typeOfContainerService.getTypeOfContainer(id);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "{id}")
    public void updateTypeOfContainer(@PathVariable long id, @RequestBody String updatedTypeOfContainerJson) {
        typeOfContainerService.updateTypeOfContainer(id, updatedTypeOfContainerJson);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "{id}")
    public void deleteTypeOfContainer(@PathVariable("id") long id) {
        typeOfContainerService.deleteTypeOfContainer(id);
    }
}
