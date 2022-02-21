package edu.senla.controller.impl;

import edu.senla.controller.ContainerTypeController;
import edu.senla.model.dto.ContainerTypeDTO;
import edu.senla.service.ContainerTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/typesOfContainer")
public class ContainerTypeControllerImpl implements ContainerTypeController {

    private final ContainerTypeService containerTypeService;

    @GetMapping
    public List<ContainerTypeDTO> getAllTypesOfContainer(@RequestParam(value = "pages", required = false, defaultValue = "10") int pages) {
        return containerTypeService.getAllTypesOfContainer(pages);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public void createTypeOfContainer(@RequestBody String typeOfContainerJson) {
        containerTypeService.createTypeOfContainer(typeOfContainerJson);
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "{id}")
    public ContainerTypeDTO getTypeOfContainer(@PathVariable("id") long id) {
        return containerTypeService.getTypeOfContainer(id);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "{id}")
    public void updateTypeOfContainer(@PathVariable long id, @RequestBody String updatedTypeOfContainerJson) {
        containerTypeService.updateTypeOfContainer(id, updatedTypeOfContainerJson);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "{id}")
    public void deleteTypeOfContainer(@PathVariable("id") long id) {
        containerTypeService.deleteTypeOfContainer(id);
    }
}
