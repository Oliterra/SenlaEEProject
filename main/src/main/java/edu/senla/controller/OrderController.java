package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.OrderControllerInterface;
import edu.senla.dto.OrderDTO;
import edu.senla.dto.OrderForUpdateDTO;
import edu.senla.exeptions.NoContent;
import edu.senla.exeptions.NotFound;
import edu.senla.service.serviceinterface.OrderServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController implements OrderControllerInterface {

    private final OrderServiceInterface orderService;

    private final ObjectMapper mapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(OrderController.class);

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getAllOrders() {
        LOG.info("Getting all orders");
        List<OrderDTO> orderDTOs = orderService.getAllOrders();
        if (orderDTOs == null || orderDTOs.isEmpty()){
            LOG.info("No orders found");
            throw new NoContent();
        }
        String ordersJson = mapper.writeValueAsString(orderDTOs);
        return new ResponseEntity<String>(ordersJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getOrder(@PathVariable("id") long id) {
        LOG.info("Getting order with id: {}", id);
        if (!orderService.isOrderExists(id)) {
            LOG.info("There is no order with id {}", id);
            throw new NotFound();
        }
        OrderDTO orderDTO = orderService.getOrder(id);
        String orderJson = mapper.writeValueAsString(orderDTO);
        return new ResponseEntity<String>(orderJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateOrder(@PathVariable long id, @RequestBody String updatedOrderJson) {
        LOG.info("Updating order with id {} with new data {}: ", id, updatedOrderJson);
        OrderForUpdateDTO updatedOrderDTO = mapper.readValue(updatedOrderJson, OrderForUpdateDTO.class);
        if (!orderService.isOrderExists(id)) {
            LOG.info("There is no order with id {}", id);
            throw new NotFound();
        }
        orderService.updateOrder(id, updatedOrderDTO);
        LOG.info("Order with id {} successfully updated", id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") long id) {
        LOG.info("Deleting order with id: {}", id);
        if (!orderService.isOrderExists(id)) {
            LOG.info("There is no order with id {}", id);
            throw new NotFound();
        }
        orderService.deleteOrder(id);
        LOG.info("Order with id {} successfully deleted", id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
