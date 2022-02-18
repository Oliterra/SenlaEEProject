package edu.senla.controller.impl;

import edu.senla.controller.OrderConfirmationController;
import edu.senla.model.dto.OrderClosingResponseDTO;
import edu.senla.service.ClientService;
import edu.senla.service.CourierService;
import edu.senla.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/confirmation")
public class OrderConfirmationControllerImpl implements OrderConfirmationController {

    private final ClientService clientService;
    private final OrderService orderService;
    private final CourierService courierService;

    @Secured({"ROLE_USER"})
    @PatchMapping(value = "{orderId}")
    public void confirmReceiptOfTheOrderClient(@PathVariable long orderId) {
        long clientId = clientService.getCurrentClientId();
        orderService.closeOrderForClient(clientId, orderId);
    }

    @Secured({"ROLE_COURIER"})
    @PatchMapping
    public OrderClosingResponseDTO closeOrderCourier() {
        long id = courierService.getCurrentCourierId();
        return orderService.closeOrderForCourier(id);
    }
}
