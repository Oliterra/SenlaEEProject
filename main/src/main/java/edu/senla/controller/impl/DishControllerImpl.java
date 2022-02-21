package edu.senla.controller.impl;

import edu.senla.controller.DishController;
import edu.senla.model.dto.DishDTO;
import edu.senla.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dishes")
public class DishControllerImpl implements DishController {

    private final DishService dishService;

    @GetMapping
    public List<DishDTO> getAllDishes(@RequestParam(value = "pages", required = false, defaultValue = "10") int pages) {
        return dishService.getAllDishes(pages);
    }

    //@Secured({"ROLE_ADMIN"})
    @PostMapping
    public void createDish(@RequestBody String dishJson) {
        dishService.createDish(dishJson);
    }

    @GetMapping(value = "{id}")
    public DishDTO getDish(@PathVariable("id") long id) {
        return dishService.getDish(id);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "{id}")
    public void updateDish(@PathVariable("id") long id, @RequestBody String updatedDishJson) {
        dishService.updateDish(id, updatedDishJson);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "{id}")
    public void deleteDish(@PathVariable("id") long id) {
        dishService.deleteDish(id);
    }
}
