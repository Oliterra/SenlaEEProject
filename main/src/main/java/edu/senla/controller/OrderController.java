package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.OrderControllerInterface;
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
@RequestMapping("/orders")
public class OrderController implements OrderControllerInterface {

    private final OrderServiceInterface orderService;

    private final ObjectMapper mapper;

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getAllOrders() {
        String ordersJson = mapper.writeValueAsString(orderService.getAllOrders());
        return new ResponseEntity<String>(ordersJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getOrder(@PathVariable("id") long id) {
        String orderJson = mapper.writeValueAsString(orderService.getOrder(id));
        return new ResponseEntity<String>(orderJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
