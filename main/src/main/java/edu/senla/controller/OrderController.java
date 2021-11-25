package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.OrderControllerInterface;
import edu.senla.dto.ClientDTO;
import edu.senla.dto.CourierDTO;
import edu.senla.dto.OrderDTO;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import edu.senla.service.serviceinterface.OrderServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderController implements OrderControllerInterface {

    private final OrderServiceInterface orderService;

    private final ClientServiceInterface clientService;

    private final CourierServiceInterface courierService;

    private final ObjectMapper jacksonObjectMapper;

    @SneakyThrows
    @Override
    public int createOrder(int clientId, String newOrderJson) {
        OrderDTO newOrderDTO = jacksonObjectMapper.readValue(newOrderJson, OrderDTO.class);
        ClientDTO client = clientService.readClient(clientId);
        int newOrderId = orderService.createOrder(clientId, newOrderDTO);
        System.out.println("Order for client " + client.getFirstName() + " " + client.getLastName() + " was successfully created");
        return newOrderId;
    }

    @SneakyThrows
    @Override
    public String readOrder(int id) {
        OrderDTO orderDTO = orderService.readOrder(id);
        return jacksonObjectMapper.writeValueAsString(orderDTO);
    }

    @SneakyThrows
    @Override
    public void updateOrder(int id, String updatedOrderJson) {
        OrderDTO updatedOrderDTO = jacksonObjectMapper.readValue(updatedOrderJson, OrderDTO.class);
        orderService.updateOrder(id, updatedOrderDTO);
        System.out.println("Order was successfully updated");
    }

    @Override
    public void deleteOrder(int id) {
        orderService.deleteOrder(id);
        System.out.println("Order was successfully deleted");
    }

    @Override
    public void setOrderCourier(int orderId, int courierId) {
        orderService.setOrderCourier(orderService.readOrder(orderId), courierId);
    }

    @SneakyThrows
    @Override
    public String getAllClientsOrders(int clientId)  {
        ClientDTO client = clientService.readClient(clientId);
        List<OrderDTO> orders = orderService.getAllClientsOrders(clientId);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        System.out.println("All orders of user " + client.getFirstName() + " " + client.getLastName());
        for (OrderDTO order: orders) {
            sb.append(++i+".").append(jacksonObjectMapper.writeValueAsString(order)).append("\n");
        }
        return sb.toString();
    }

    @SneakyThrows
    @Override
    public String getAllCouriersOrders(int courierId) {
        CourierDTO courier = courierService.readCourier(courierId);
        List<OrderDTO> orders = orderService.getAllCouriersOrders(courierId);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        System.out.println("All orders of courier " + courier.getFirstName() + " " + courier.getLastName());
        for (OrderDTO order: orders) {
            sb.append(++i+".").append(jacksonObjectMapper.writeValueAsString(order)).append("\n");
        }
        return sb.toString();
    }

}
