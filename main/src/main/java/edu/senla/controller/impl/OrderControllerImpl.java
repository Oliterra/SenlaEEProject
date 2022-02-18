package edu.senla.controller.impl;

import edu.senla.controller.OrderController;
import edu.senla.model.dto.OrderDTO;
import edu.senla.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    @Secured({"ROLE_ADMIN"})
    @GetMapping
    public List<OrderDTO> getAllOrders(@RequestParam(value = "pages", required = false, defaultValue = "10") int pages) {
        return orderService.getAllOrders(pages);
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "{id}")
    public OrderDTO getOrder(@PathVariable("id") long id) {
        return orderService.getOrder(id);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "{id}")
    public void deleteOrder(@PathVariable("id") long id) {
        orderService.deleteOrder(id);
    }
}
