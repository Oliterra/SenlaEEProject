package edu.senla.dao;

import edu.senla.config.DatabaseConfig;
import edu.senla.entity.Courier;
import edu.senla.entity.Order;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = {DatabaseConfig.class,
        Courier.class, CourierRepository.class})
public class CourierRepositoryTest {

    @Autowired
    private CourierRepository courierRepository;

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

        createdCourier = courierRepository.create(courier);
    }

    @Test
    public void createCourier() {
        assertEquals(courierId, createdCourier.getId());
        assertEquals(courierFirstName, createdCourier.getFirstName());
        assertEquals(courierLastName, createdCourier.getLastName());
    }

    @Test
    public void createNullCourier() {
        assertThrows(IllegalArgumentException.class, () -> courierRepository.create(invalidCourier));
    }

    @Test
    public void readCourier() {
        Courier readCourier = courierRepository.read(createdCourier.getId());
        assertEquals(courierId, readCourier.getId());
        assertEquals(courierFirstName, readCourier.getFirstName());
        assertEquals(courierLastName, readCourier.getLastName());
    }

    @Test
    public void readNullCourier() {
        assertThrows(NullPointerException.class, () -> courierRepository.read(invalidCourier.getId()));
    }

    @Test
    public void updateCourier() {
        Courier courierToUpdate = courierRepository.read(createdCourier.getId());

        String newFirstName = "Another first name";
        String newLastName = "Another last name";
        courierToUpdate.setFirstName(newFirstName);
        courierToUpdate.setLastName(newLastName);

        Courier updatedCourier = courierRepository.update(courierToUpdate);

        assertEquals(newFirstName, updatedCourier.getFirstName());
        assertEquals(newLastName, updatedCourier.getLastName());
    }

    @Test
    public void updateNullCourier() {
        assertThrows(IllegalArgumentException.class, () -> courierRepository.update(invalidCourier));
    }

    @Test
    public void deleteCourier() {
        courierRepository.delete(createdCourier.getId());
        Courier deletedCourier = courierRepository.read(createdCourier.getId());
        assertNull(deletedCourier);
    }

    @Test
    public void deleteNullCourier() {
        assertThrows(NullPointerException.class, () -> courierRepository.delete(invalidCourier.getId()));
    }

    @Test
    public void getCourierByPhone() {
        Courier receivedCourier = courierRepository.getCourierByPhone(courierPhone);
        assertEquals(createdCourier, receivedCourier);
    }

    @Test
    public void getNullCourierByPhone() {
        assertThrows(NullPointerException.class, () -> courierRepository.getCourierByPhone(invalidCourier.getPhone()));
    }

    @Test
    public void getByIdWithOrders() {
        Courier courierWithOrders = courierRepository.getByIdWithOrders(createdCourier.getId());
        assertEquals(couriersOrders, courierWithOrders.getOrders());
    }

    @Test
    public void getNullCourierByIdWithOrders() {
        assertThrows(NullPointerException.class, () -> courierRepository.getByIdWithOrders(invalidCourier.getId()));
    }

}
