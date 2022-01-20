package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.OrdersHistoryControllerInterface;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import edu.senla.service.serviceinterface.CourierServiceInterface;
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
@RequestMapping("/history")
public class OrdersHistoryController implements OrdersHistoryControllerInterface {

    private final ClientServiceInterface clientService;

    private final CourierServiceInterface courierService;

    private final ObjectMapper mapper;

    @SneakyThrows
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "clients/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getClientOrdersHistory(@PathVariable long id) {
        String clientOrderInfoJson = mapper.writeValueAsString(clientService.getAllOrdersOfClient(id));
        return new ResponseEntity<String>(clientOrderInfoJson, HttpStatus.OK);
    }

    @SneakyThrows
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "orders/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getCourierOrdersHistory(@PathVariable long id) {
        String courierOrderInfoJson = mapper.writeValueAsString(courierService.getAllOrdersOfCourier(id));
        return new ResponseEntity<String>(courierOrderInfoJson, HttpStatus.OK);
    }

}
