package edu.senla.controller.impl;

import edu.senla.controller.StaffController;
import edu.senla.model.dto.CourierBasicInfoDTO;
import edu.senla.model.dto.CourierPerformanceIndicatorDTO;
import edu.senla.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/staff")
public class StaffControllerImpl implements StaffController {

    private final CourierService courierService;

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "{id}")
    public CourierPerformanceIndicatorDTO getCouriersPerformanceIndicator(@PathVariable long id) {
        return courierService.getCourierPerformanceIndicator(id);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping
    public void assignOrdersToAllCouriers() {
        List<CourierBasicInfoDTO> courierBasicInfoDTOS = courierService.getAllActiveCouriersDTO();
        courierService.assignOrdersToAllActiveCouriers(courierBasicInfoDTOS);
    }
}
