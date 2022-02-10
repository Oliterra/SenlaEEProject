package edu.senla.controller.impl;

import edu.senla.controller.DishInformationController;
import edu.senla.model.dto.DishInformationDTO;
import edu.senla.service.DishInformationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dishesInformation")
public class DishInformationControllerImpl implements DishInformationController {

    private final DishInformationService dishInformationService;

    @SneakyThrows
    @GetMapping
    public List<DishInformationDTO> getAllDishesInformation(@RequestParam(value = "pages", required = false, defaultValue = "10") int pages) {
        return dishInformationService.getAllDishesInformation(pages);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public void createDishInformation(@RequestBody String dishInformationJson) {
        dishInformationService.createDishInformation(dishInformationJson);
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "{id}")
    public DishInformationDTO getDishInformation(@PathVariable("id") long id) {
        return dishInformationService.getDishInformation(id);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "{id}")
    public void updateDishInformation(@PathVariable long id, @RequestBody String updatedDishInformationJson) {
        dishInformationService.updateDishInformation(id, updatedDishInformationJson);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "{id}")
    public void deleteDishInformation(@PathVariable("id") int id) {
        dishInformationService.deleteDishInformation(id);
    }
}
