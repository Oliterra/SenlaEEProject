package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.OrderControllerInterface;
import edu.senla.dto.ClientDTO;
import edu.senla.dto.OrderDTO;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import edu.senla.service.serviceinterface.OrderServiceInterface;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController implements OrderControllerInterface {

    @Autowired
    private OrderServiceInterface orderService;

    @Autowired
    private ClientServiceInterface clientService;

    @Autowired
    private CourierServiceInterface courierService;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(OrderController.class);

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createOrder(@RequestBody String orderJson) {
        LOG.info("Creating new order: {}", orderJson);
        OrderDTO orderDTO = jacksonObjectMapper.readValue(orderJson, OrderDTO.class);
        int clientId = orderDTO.getClientId();

        try {
            ClientDTO clientDTO = clientService.readClient(clientId);
        } catch (IllegalArgumentException exception) {
            LOG.info("Client for whom the order is being created with id {} not found", clientId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        orderService.createOrder(clientId, orderDTO);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getOrder(@PathVariable("id") int id) {
        LOG.info("Getting order with id: {}", id);

        OrderDTO orderDTO;
        try {
            orderDTO = orderService.readOrder(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Order with id {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>(jacksonObjectMapper.writeValueAsString(orderDTO), HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateOrder(@PathVariable int id, @RequestBody String updatedOrderJson) {
        LOG.info("Updating order: ");

        try {
            OrderDTO currentOrder = orderService.readOrder(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Order with id {} not found", id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        OrderDTO updatedOrder = jacksonObjectMapper.readValue(updatedOrderJson, OrderDTO.class);

        orderService.updateOrder(id, updatedOrder);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") int id) {
        LOG.info("Deleting order with id: {}", id);

        try {
            OrderDTO orderDTO = orderService.readOrder(id);
        } catch (IllegalArgumentException exception) {
            LOG.info("Unable to delete. Order with id {} not found", id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        orderService.deleteOrder(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
