package edu.senla.controller.impl;

import edu.senla.controller.ShoppingCartController;
import edu.senla.model.dto.OrderTotalCostDTO;
import edu.senla.service.UserService;
import edu.senla.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shoppingCart")
public class ShoppingCartControllerImpl implements ShoppingCartController {

    private final UserService userService;
    private final OrderService orderService;

    @PostMapping
    //@Secured({"ROLE_USER"})
    public OrderTotalCostDTO makeOrder(@RequestBody String shoppingCartJson) {
        long clientId = userService.getCurrentClientId();
        return orderService.checkIncomingOrderDataAndCreateIfItIsCorrect(clientId, shoppingCartJson);
    }
}
