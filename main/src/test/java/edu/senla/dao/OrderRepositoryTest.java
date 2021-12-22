package edu.senla.dao;

import edu.senla.entity.Client;
import edu.senla.entity.Courier;
import edu.senla.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepositoryInterface orderRepository;

    @Autowired
    private ClientRepositoryInterface clientRepository;

    @Autowired
    private CourierRepositoryInterface courierRepository;

    private Order createdOrder;
    private Order invalidOrder;

    private int orderId;
    private String orderPaymentType;
    private String orderStatus;
    private Client orderClient;
    private Courier orderCourier;
    private LocalDate orderDate;

    @BeforeEach
    public void setup() {
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
        orderClient = clientRepository.save(client);

        Courier courier = new Courier();
        courier.setId(1);
        courier.setFirstName("TestFirstName");
        courier.setLastName("TestLastName");
        courier.setPhone("TestPhone");
        orderCourier = courierRepository.save(courier);

        Order order = new Order();

        order.setId(orderId);
        order.setPaymentType(orderPaymentType);
        order.setStatus(orderStatus);
        order.setClient(orderClient);
        order.setCourier(orderCourier);
        order.setDate(orderDate);

        createdOrder = orderRepository.save(order);
    }

}
