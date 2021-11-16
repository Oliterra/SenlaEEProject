package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.OrderControllerInterface;
import edu.senla.dto.OrderDTO;
import edu.senla.service.serviceinterface.OrderServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderController implements OrderControllerInterface {

    private final OrderServiceInterface orderService;

    private final ObjectMapper jacksonObjectMapper;

    @SneakyThrows
    @Override
    public void createOrder(String newOrderJson) {
        OrderDTO newOrderDTO = jacksonObjectMapper.readValue(newOrderJson, OrderDTO.class);
        orderService.createOrder(newOrderDTO);
        System.out.println("Order" + readOrder(newOrderDTO.getId()) + " was successfully created");
    }

    @SneakyThrows
    @Override
    public String readOrder(int id) {
        OrderDTO orderDTO = orderService.read(id);
        return jacksonObjectMapper.writeValueAsString(orderDTO);
    }

    @SneakyThrows
    @Override
    public void updateOrder(int id, String updatedOrderJson) {
        OrderDTO updatedOrderDTO = jacksonObjectMapper.readValue(updatedOrderJson, OrderDTO.class);
        orderService.update(id, updatedOrderDTO);
        System.out.println("Order was successfully updated");
    }

    @Override
    public void deleteOrder(int id) {
        orderService.delete(id);
        System.out.println("Order was successfully deleted");
    }

}
