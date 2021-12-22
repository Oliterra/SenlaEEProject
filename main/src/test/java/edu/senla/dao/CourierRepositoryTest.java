package edu.senla.dao;

import edu.senla.entity.Courier;
import edu.senla.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class CourierRepositoryTest {

    @Autowired
    private CourierRepositoryInterface courierRepository;

    private Courier createdCourier;
    private Courier invalidCourier;

    private int courierId;
    private String courierFirstName;
    private String courierLastName;
    private String courierPhone;
    private List<Order> couriersOrders;

    @BeforeEach
    public void setup() {
        courierId = 1;
        courierFirstName = "TestFirstName";
        courierLastName = "TestLastName";
        courierPhone = "+375333333333";

        couriersOrders = new ArrayList<>();
        Order order = new Order();
        order.setPaymentType("by cash");
        couriersOrders.add(order);

        Courier courier = new Courier();

        courier.setId(courierId);
        courier.setFirstName(courierFirstName);
        courier.setLastName(courierLastName);
        courier.setPhone(courierPhone);
        courier.setOrders(couriersOrders);

        createdCourier = courierRepository.save(courier);
    }

}
