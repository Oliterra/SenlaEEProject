package edu.senla.service;

import edu.senla.dao.ClientRepository;
import edu.senla.dao.OrderRepository;
import edu.senla.dto.OrderDTO;
import edu.senla.entity.Client;
import edu.senla.entity.Order;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientRepository clientRepository;

    @Spy
    private ModelMapper mapper;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private Client client;

    private int orderId;
    private String orderPaymentType;
    private String orderStatus;

    @BeforeEach
    void setup() {
        orderId = 1;
        orderPaymentType = "by card";
        orderStatus = "new";

        order = new Order();

        order.setId(orderId);
        order.setStatus(orderStatus);
        order.setPaymentType(orderPaymentType);

        client = new Client();
        client.setId(1);
    }

    @Test
    void createOrder() {
        when(orderRepository.create(any(Order.class))).thenReturn(order);
        when(clientRepository.read(any(Integer.class))).thenReturn(client);

        OrderDTO orderParamsDTO = new OrderDTO(orderId, orderStatus, orderPaymentType);
        OrderDTO createdOrderDTO = orderService.createOrder(1, orderParamsDTO);

        verify(orderRepository, times(1)).create(any());

        Assert.assertEquals(orderId, createdOrderDTO.getId());
        Assert.assertEquals(orderStatus, createdOrderDTO.getStatus());
        Assert.assertEquals(orderPaymentType, createdOrderDTO.getPaymentType());
    }

    @Test
    void createNullOrder() {
        OrderDTO invalidOrderDTO = null;
        Assert.assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(1, invalidOrderDTO));
    }

    @Test
    void readOrder() {
        when(orderRepository.read(any(Integer.class))).thenReturn(order);

        OrderDTO readOrderDTO = orderService.readOrder(orderId);

        verify(orderRepository, times(1)).read(any());

        Assert.assertEquals(orderId, readOrderDTO.getId());
        Assert.assertEquals(orderStatus, readOrderDTO.getStatus());
        Assert.assertEquals(orderPaymentType, readOrderDTO.getPaymentType());
    }

    @Test
    void readNullOrder() {
        Assert.assertThrows(IllegalArgumentException.class, () -> orderService.readOrder(0));
    }

    @Test
    void updateOrder() {
        String newStatus = "in process";
        String newPaymentTYpe = "cash";

        Order updatedOrder = new Order();
        updatedOrder.setId(orderId);
        updatedOrder.setPaymentType(newPaymentTYpe);
        updatedOrder.setStatus(newStatus);

        when(orderRepository.update(any(Order.class))).thenReturn(updatedOrder);
        when(orderRepository.read(any(Integer.class))).thenReturn(order);

        OrderDTO updatedOrderParamsDTO = new OrderDTO(orderId, newPaymentTYpe, newStatus);
        OrderDTO updatedOrderDTO = orderService.updateOrder(orderId, updatedOrderParamsDTO);

        verify(orderRepository, times(1)).update(any());

        Assert.assertEquals(orderId, updatedOrderDTO.getId());
        Assert.assertEquals(newStatus, updatedOrderDTO.getStatus());
        Assert.assertEquals(newPaymentTYpe, updatedOrderDTO.getPaymentType());
    }

    @Test
    void updateNullOrder() {
        String newStatus = "in process";
        String newPaymentTYpe = "cash";
        OrderDTO updatedOrderParamsDTO = new OrderDTO(orderId, newPaymentTYpe, newStatus);

        OrderDTO invalidOrderDTO = null;
        Assert.assertThrows(NullPointerException.class, () -> orderService.updateOrder(invalidOrderDTO.getId(), updatedOrderParamsDTO));
    }

    @Test
    void updateOrderWithNullParams() {
        OrderDTO invalidNewOrderParamsDTO = null;

        Assert.assertThrows(IllegalArgumentException.class, () -> orderService.updateOrder(orderId, invalidNewOrderParamsDTO));
    }

    @Test
    void deleteOrder() {
        orderService.deleteOrder(orderId);
        verify(orderRepository, times(1)).delete(any());
    }

    @Test
    void deleteNullOrder() {
        OrderDTO invalidOrderDTO = null;
        Assert.assertThrows(NullPointerException.class, () -> orderService.deleteOrder(invalidOrderDTO.getId()));
    }

}