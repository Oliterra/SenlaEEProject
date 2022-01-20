package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.OrderConfirmationControllerInterface;
import edu.senla.dto.OrderClosingResponseDTO;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import edu.senla.service.serviceinterface.OrderServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/confirmation")
public class OrderConfirmationController implements OrderConfirmationControllerInterface {

    private final ClientServiceInterface clientService;

    private final OrderServiceInterface orderService;

    private final CourierServiceInterface courierService;

    private final ObjectMapper mapper;

    @SneakyThrows
    @Secured({"ROLE_USER"})
    @RequestMapping(value = "{orderId}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> confirmReceiptOfTheOrderClient(@PathVariable long orderId) {
        long clientId = clientService.getCurrentClientId();
        orderService.closeOrderForClient(clientId, orderId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @SneakyThrows
    @Secured({"ROLE_COURIER"})
    @RequestMapping(method = RequestMethod.PATCH)
    public ResponseEntity<String> closeOrderCourier() {
        long id = courierService.getCurrentCourierId();
        OrderClosingResponseDTO orderClosingResponseDTO = orderService.closeOrderForCourier(id);
        String orderClosingResponseJson = mapper.writeValueAsString(orderClosingResponseDTO);
        return new ResponseEntity<String>(orderClosingResponseJson, HttpStatus.OK);
    }

}
