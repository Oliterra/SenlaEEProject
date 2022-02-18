package edu.senla.controller.impl;

import edu.senla.controller.OrdersHistoryController;
import edu.senla.model.dto.ClientOrderInfoDTO;
import edu.senla.model.dto.CourierOrderInfoDTO;
import edu.senla.service.ClientService;
import edu.senla.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class OrdersHistoryControllerImpl implements OrdersHistoryController {

    private final ClientService clientService;
    private final CourierService courierService;

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "clients/{id}")
    public List<ClientOrderInfoDTO> getClientOrdersHistory(@PathVariable long id) {
        return clientService.getAllOrdersOfClient(id);
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "orders/{id}")
    public List<CourierOrderInfoDTO> getCourierOrdersHistory(@PathVariable long id) {
        return courierService.getAllOrdersOfCourier(id);
    }
}
