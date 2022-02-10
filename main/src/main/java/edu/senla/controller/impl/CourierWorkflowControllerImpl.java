package edu.senla.controller.impl;

import edu.senla.controller.CourierWorkflowController;
import edu.senla.model.dto.CourierCurrentOrderInfoDTO;
import edu.senla.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workflows")
public class CourierWorkflowControllerImpl implements CourierWorkflowController {

    private final CourierService courierService;

    @Secured({"ROLE_COURIER"})
    @GetMapping(value = "/order")
    public CourierCurrentOrderInfoDTO getCurrentOrderInfo() {
        long id = courierService.getCurrentCourierId();
        return courierService.getCurrentOrderForCourier(id);
    }

    @Secured({"ROLE_COURIER"})
    @GetMapping(value = "/status")
    public void changeStatus() {
        long id = courierService.getCurrentCourierId();
        courierService.changeCourierStatus(id);
    }

    @Secured({"ROLE_COURIER"})
    @PutMapping(value = "/orders")
    public CourierCurrentOrderInfoDTO getNewOrder() {
        long id = courierService.getCurrentCourierId();
        courierService.assignNewOrdersToCourier(id);
        return courierService.getCurrentOrderForCourier(id);
    }
}
