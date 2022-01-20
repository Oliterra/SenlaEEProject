package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.ShoppingCartControllerInterface;
import edu.senla.dto.OrderTotalCostDTO;
import edu.senla.dto.ShoppingCartDTO;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import edu.senla.service.serviceinterface.OrderServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shoppingCart")
public class ShoppingCartController implements ShoppingCartControllerInterface {

    private final ClientServiceInterface clientService;

    private final OrderServiceInterface orderService;

    private final ObjectMapper mapper;

    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST)
    @Secured({"ROLE_USER"})
    public ResponseEntity<String> makeOrder(@RequestBody String shoppingCartJson) {
        ShoppingCartDTO shoppingCartDTO = mapper.readValue(shoppingCartJson, ShoppingCartDTO.class);
        long clientId = clientService.getCurrentClientId();
        OrderTotalCostDTO totalCost = orderService.checkIncomingOrderDataAndCreateIfItIsCorrect(clientId, shoppingCartDTO);
        String totalCostJson = mapper.writeValueAsString(totalCost);
        return new ResponseEntity<String>(totalCostJson, HttpStatus.OK);
    }

}
