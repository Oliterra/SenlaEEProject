package edu.senla.service;

import edu.senla.dao.CourierRepository;
import edu.senla.dto.CourierDTO;
import edu.senla.entity.Courier;
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

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    @Mock
    private CourierRepository courierRepository;

    @Spy
    private ModelMapper mapper;

    @InjectMocks
    private CourierService courierService;

    private Courier courier;

    private int courierId;
    private String courierFirstName;
    private String courierLastName;

    @BeforeEach
    void setup() {
        courierId = 1;
        courierFirstName = "TestFirstName";
        courierLastName = "TestLastName";

        courier = new Courier();

        courier.setId(courierId);
        courier.setFirstName(courierFirstName);
        courier.setLastName(courierLastName);
    }

    @Test
    void createCourier() {
        when(courierRepository.create(any(Courier.class))).thenReturn(courier);

        CourierDTO courierParamsDTO = new CourierDTO(courierId, courierFirstName, courierLastName);
        CourierDTO createdCourierDTO = courierService.createCourier(courierParamsDTO);

        verify(courierRepository, times(1)).create(any());

        Assert.assertEquals(courierId, createdCourierDTO.getId());
        Assert.assertEquals(courierFirstName, createdCourierDTO.getFirstName());
        Assert.assertEquals(courierLastName, createdCourierDTO.getLastName());
    }

    @Test
    void createNullCourier() {
        CourierDTO invalidCourierDTO = null;
        Assert.assertThrows(IllegalArgumentException.class, () -> courierService.createCourier(invalidCourierDTO));
    }

    @Test
    void readCourier() {
        when(courierRepository.read(any(Integer.class))).thenReturn(courier);

        CourierDTO readCourierDTO = courierService.readCourier(courierId);

        verify(courierRepository, times(1)).read(any());

        Assert.assertEquals(courierId, readCourierDTO.getId());
        Assert.assertEquals(courierFirstName, readCourierDTO.getFirstName());
        Assert.assertEquals(courierLastName, readCourierDTO.getLastName());
    }

    @Test
    void readNullCourier() {
        Assert.assertThrows(IllegalArgumentException.class, () -> courierService.readCourier(0));
    }

    @Test
    void updateCourier() {
        String newFirstName = "Another first name";
        String newLastName = "Another last name";

        Courier updatedCourier = new Courier();
        updatedCourier.setId(courierId);
        updatedCourier.setFirstName(newFirstName);
        updatedCourier.setLastName(newLastName);

        when(courierRepository.update(any(Courier.class))).thenReturn(updatedCourier);
        when(courierRepository.read(any(Integer.class))).thenReturn(courier);

        CourierDTO updatedCourierParamsDTO = new CourierDTO(courierId, newFirstName, newLastName);
        CourierDTO updatedCourierDTO = courierService.updateCourier(courierId, updatedCourierParamsDTO);

        verify(courierRepository, times(1)).update(any());

        Assert.assertEquals(courierId, updatedCourierDTO.getId());
        Assert.assertEquals(newFirstName, updatedCourierDTO.getFirstName());
        Assert.assertEquals(newLastName, updatedCourierDTO.getLastName());
    }

    @Test
    void updateNullCourier() {
        String newFirstName = "Another first name";
        String newLastName = "Another last name";
        CourierDTO updatedCourierParamsDTO = new CourierDTO(courierId, newFirstName, newLastName);

        CourierDTO invalidCourierDTO= null;
        Assert.assertThrows(NullPointerException.class, () -> courierService.updateCourier(invalidCourierDTO.getId(), updatedCourierParamsDTO));
    }

    @Test
    void updateCourierWithNullParams() {
        CourierDTO invalidUpdatedCourierParamsDTO = null;

        Assert.assertThrows(IllegalArgumentException.class, () -> courierService.updateCourier(courierId, invalidUpdatedCourierParamsDTO));
    }

    @Test
    void deleteCourier() {
        courierRepository.delete(courierId);
        verify(courierRepository, times(1)).delete(any());
    }

    @Test
    void deleteNullCourier() {
        CourierDTO invalidCourierDTO = null;
        Assert.assertThrows(NullPointerException.class, () -> courierService.deleteCourier(invalidCourierDTO.getId()));
    }

    @Test
    void getCourierByIdWithOrders() {
        List<Order> ordersList = new ArrayList<>();
        Order order = new Order();
        order.setId(1);
        order.setStatus("new");
        ordersList.add(order);

        courier.setOrders(ordersList);
        when(courierRepository.getByIdWithOrders(any(Integer.class))).thenReturn(courier);

        CourierDTO courierWithOrdersDTO = courierService.getByIdWithOrders(courierId);

        verify(courierRepository, times(1)).getByIdWithOrders(any(Integer.class));

        Assert.assertEquals(courierId, courierWithOrdersDTO.getId());
        Assert.assertEquals(courierFirstName, courierWithOrdersDTO.getFirstName());
        Assert.assertEquals(courierLastName, courierWithOrdersDTO.getLastName());
        Assert.assertEquals(ordersList, courierWithOrdersDTO.getOrders());
    }

    @Test
    void getNullCourierByIdWithOrders() {
        Assert.assertThrows(IllegalArgumentException.class, () -> courierService.getByIdWithOrders(courier.getId()));
    }

    @Test
    void getCourierByIdWithNullOrders() {
        List<Order> ordersList = null;
        courier.setOrders(ordersList);

        when(courierRepository.getByIdWithOrders(any(Integer.class))).thenReturn(courier);

        CourierDTO courierWithOrdersDTO = courierService.getByIdWithOrders(courierId);

        verify(courierRepository, times(1)).getByIdWithOrders(any(Integer.class));

        Assert.assertEquals(courierId, courierWithOrdersDTO.getId());
        Assert.assertEquals(courierFirstName, courierWithOrdersDTO.getFirstName());
        Assert.assertEquals(courierLastName, courierWithOrdersDTO.getLastName());
        Assert.assertNull(courierWithOrdersDTO.getOrders());
    }

    @Test
    void isExistentCourierExists() {
        String courierPhone = "+375333333333";
        courier.setPhone(courierPhone);
        when(courierRepository.getCourierByPhone(any(String.class))).thenReturn(courier);

        CourierDTO courierWithPhoneDTO = new CourierDTO();
        courierWithPhoneDTO.setPhone(courierPhone);

        boolean courierExists = courierService.isCourierExists(courierWithPhoneDTO);

        verify(courierRepository, times(1)).getCourierByPhone(any(String.class));

        Assert.assertTrue(courierExists);
    }

    @Test
    void isNonExistentCourierExists() {
        String courierPhone = "+375333333333";

        when(courierRepository.getCourierByPhone(any(String.class))).thenThrow(new NoResultException());

        CourierDTO courierWithPhoneDTO = new CourierDTO();
        courierWithPhoneDTO.setPhone(courierPhone);

        boolean courierExists = courierService.isCourierExists(courierWithPhoneDTO);

        verify(courierRepository, times(1)).getCourierByPhone(any(String.class));

        Assert.assertThrows(NoResultException.class, () -> courierRepository.getCourierByPhone(courierPhone));
        Assert.assertFalse(courierExists);
    }

    @Test
    void isNullCourierExists() {
        CourierDTO invalidCourierWithEmailDTO = null;

        Assert.assertThrows(NullPointerException.class, () -> courierService.isCourierExists(invalidCourierWithEmailDTO));
    }

}