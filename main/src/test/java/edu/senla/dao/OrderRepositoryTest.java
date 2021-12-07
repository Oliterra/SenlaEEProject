package edu.senla.dao;

import edu.senla.config.DatabaseConfig;
import edu.senla.entity.Client;
import edu.senla.entity.Courier;
import edu.senla.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = {DatabaseConfig.class,
        Order.class, OrderRepository.class,
        Client.class, ClientRepository.class,
        Courier.class, CourierRepository.class})
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CourierRepository courierRepository;

    private Order createdOrder;
    private Order invalidOrder;

    private int orderId;
    private String orderPaymentType;
    private String orderStatus;
    private Client orderClient;
    private Courier orderCourier;
    private LocalDate orderDate;

    @BeforeEach
    void setup() {
        orderId = 1;
        orderPaymentType = "TestPaymentType";
        orderStatus = "New";
        orderDate = LocalDate.now();

        Client client= new Client();
        client.setId(1);
        client.setFirstName("TestFirstName");
        client.setLastName("TestLastName");
        client.setPhone("TestPhone");
        client.setEmail("TestEmail");
        client.setAddress("TestAdderess");
        orderClient = clientRepository.create(client);

        Courier courier = new Courier();
        courier.setId(1);
        courier.setFirstName("TestFirstName");
        courier.setLastName("TestLastName");
        courier.setPhone("TestPhone");
        orderCourier = courierRepository.create(courier);

        Order order = new Order();

        order.setId(orderId);
        order.setPaymentType(orderPaymentType);
        order.setStatus(orderStatus);
        order.setClient(orderClient);
        order.setCourier(orderCourier);
        order.setDate(orderDate);

        createdOrder = orderRepository.create(order);
    }

    @Test
    void createOrder() {
        assertEquals(orderId, createdOrder.getId());
        assertEquals(orderPaymentType, createdOrder.getPaymentType());
        assertEquals(orderStatus, createdOrder.getStatus());
    }

    @Test
    void createNullOrder() {
        assertThrows(IllegalArgumentException.class, () -> orderRepository.create(invalidOrder));
    }

    @Test
    void readOrder() {
        Order readOrder = orderRepository.read(createdOrder.getId());
        assertEquals(orderId, readOrder.getId());
        assertEquals(orderPaymentType, readOrder.getPaymentType());
        assertEquals(orderStatus, readOrder.getStatus());
    }

    @Test
    void readNullOrder() {
        assertThrows(NullPointerException.class, () -> orderRepository.read(invalidOrder.getId()));
    }

    @Test
    void updateOrder() {
        Order orderToUpdate = orderRepository.read(createdOrder.getId());

        String newPaymentType = "AnotherPaymentType";
        String newStatus = "In process";
        orderToUpdate.setPaymentType(newPaymentType);
        orderToUpdate.setStatus(newStatus);

        Order updatedOrder = orderRepository.update(orderToUpdate);

        assertEquals(newPaymentType, updatedOrder.getPaymentType());
        assertEquals(newStatus, updatedOrder.getStatus());
    }

    @Test
    void updateNullOrder() {
        assertThrows(IllegalArgumentException.class, () -> orderRepository.update(invalidOrder));
    }

    @Test
    void deleteOrder() {
        orderRepository.delete(createdOrder.getId());
        Order deletedOrder = orderRepository.read(createdOrder.getId());
        assertNull(deletedOrder);
    }

    @Test
    void deleteNullOrder() {
        assertThrows(NullPointerException.class, () -> orderRepository.delete(invalidOrder.getId()));
    }

    @Test
    void getAllClientsOrders() {
        List<Order> ordersForChecking = new ArrayList<>();
        ordersForChecking.add(createdOrder);

        List<Order> clientsOrders = orderRepository.getAllClientsOrders(1);

        assertEquals(ordersForChecking, clientsOrders);
    }

    @Test
    void getNullClientsOrders() {
        Client invalidClient = null;
        assertThrows(NullPointerException.class, () -> orderRepository.getAllClientsOrders(invalidClient.getId()));
    }

    @Test
    void getAllCouriersOrders() {
        List<Order> ordersForChecking = new ArrayList<>();
        ordersForChecking.add(createdOrder);

        List<Order> couriersOrders = orderRepository.getAllCouriersOrders(1);

        assertEquals(ordersForChecking, couriersOrders);
    }

    @Test
    void getNullCouriersOrders() {
        Courier invalidCourier = null;
        assertThrows(NullPointerException.class, () -> orderRepository.getAllCouriersOrders(invalidCourier.getId()));
    }

}