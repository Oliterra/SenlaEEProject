package edu.senla.controller.impl;

import edu.senla.controller.CourierController;
import edu.senla.model.dto.CourierMainInfoDTO;
import edu.senla.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/couriers")
public class CourierControllerImpl implements CourierController {

    private final CourierService courierService;

    @Secured({"ROLE_ADMIN"})
    @GetMapping
    public List<CourierMainInfoDTO> getAllCouriers(@RequestParam(value = "pages", required = false, defaultValue = "10") int pages) {
        return courierService.getAllCouriers(pages);
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "{id}")
    public CourierMainInfoDTO getCourier(@PathVariable("id") long id) {
        return courierService.getCourier(id);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "{id}")
    public void updateCourier(@PathVariable long id, @RequestBody String updatedCourierJson) {
        courierService.updateCourier(id, updatedCourierJson);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "{id}")
    public void deleteCourier(@PathVariable("id") long id) {
        courierService.deleteCourier(id);
    }
}
