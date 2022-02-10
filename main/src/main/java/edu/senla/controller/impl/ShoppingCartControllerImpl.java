package edu.senla.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.ShoppingCartController;
import edu.senla.model.dto.OrderTotalCostDTO;
import edu.senla.model.dto.ShoppingCartDTO;
import edu.senla.service.ClientService;
import edu.senla.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shoppingCart")
public class ShoppingCartControllerImpl implements ShoppingCartController {

    private final ClientService clientService;
    private final OrderService orderService;
    private final ObjectMapper mapper;

    @SneakyThrows
    @PostMapping
    @Secured({"ROLE_USER"})
    public OrderTotalCostDTO makeOrder(@RequestBody String shoppingCartJson) {
        ShoppingCartDTO shoppingCartDTO = mapper.readValue(shoppingCartJson, ShoppingCartDTO.class);
        long clientId = clientService.getCurrentClientId();
        return orderService.checkIncomingOrderDataAndCreateIfItIsCorrect(clientId, shoppingCartDTO);
    }
}
