package edu.senla.service;

import edu.senla.dao.ClientRepository;
import edu.senla.dao.ContainerRepository;
import edu.senla.dao.CourierRepository;
import edu.senla.dao.OrderRepository;
import edu.senla.model.dto.*;
import edu.senla.model.entity.Client;
import edu.senla.model.entity.Courier;
import edu.senla.model.entity.Order;
import edu.senla.model.enums.CourierStatus;
import edu.senla.model.enums.OrderPaymentType;
import edu.senla.model.enums.OrderStatus;
import edu.senla.exeption.BadRequest;
import edu.senla.exeption.ConflictBetweenData;
import edu.senla.exeption.NotFound;
import edu.senla.service.impl.ContainerServiceImpl;
import edu.senla.service.impl.CourierServiceImpl;
import edu.senla.service.impl.ValidationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ContainerRepository containerRepository;

    @Mock
    private ContainerServiceImpl containerService;

    @Spy
    private ModelMapper mapper;

    @Spy
    private ValidationServiceImpl validationService;

    @Spy
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CourierServiceImpl courierService;

    @Test
    void testGetAllCouriers() {
        List<Courier> couriersList = new ArrayList<>();
        Courier courier  = new Courier();
        courier.setStatus(CourierStatus.ACTIVE);
        couriersList.add(courier);
        Page<Courier> couriers = new PageImpl<>(couriersList);
        when(courierRepository.findAll(any(Pageable.class))).thenReturn(couriers);
        List<CourierMainInfoDTO> courierMainInfoDTOs = courierService.getAllCouriers(10);
        verify(courierRepository, times(1)).findAll((Pageable)any());
        assertTrue(courierMainInfoDTOs.size() == 1);
        assertEquals(CourierStatus.ACTIVE.toString(), courierMainInfoDTOs.get(0).getStatus());
    }

    @Test
    void testGetAllCouriersWhenThereAreNoCouriers() {
        List<Courier> couriersList = new ArrayList<>();
        Page<Courier> couriers = new PageImpl<>(couriersList);
        when(courierRepository.findAll(any(Pageable.class))).thenReturn(couriers);
        List<CourierMainInfoDTO> courierMainInfoDTOs = courierService.getAllCouriers(10);
        verify(courierRepository, times(1)).findAll((Pageable)any());
        assertTrue(courierMainInfoDTOs.isEmpty());
    }

    @Test
    void testCreateCourierWithIncorrectSymbolsInFirstName() {
        CourierRegistrationRequestDTO courierRegistrationRequestDTO = new CourierRegistrationRequestDTO();
        courierRegistrationRequestDTO.setFirstName("@!*%");
        assertThrows(BadRequest.class, () ->  courierService.createCourier(courierRegistrationRequestDTO));
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(courierRepository, never()).getByPhone(any());
        verify(passwordEncoder, never()).encode(any());
        verify(courierRepository, never()).save(any());
    }

    @Test
    void testCreateCourierWithTooShortFirstName() {
        CourierRegistrationRequestDTO courierRegistrationRequestDTO = new CourierRegistrationRequestDTO();
        courierRegistrationRequestDTO.setFirstName("c");
        assertThrows(BadRequest.class, () ->  courierService.createCourier(courierRegistrationRequestDTO));
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(courierRepository, never()).getByPhone(any());
        verify(passwordEncoder, never()).encode(any());
        verify(courierRepository, never()).save(any());
    }

    @Test
    void testCreateCourierWithIncorrectSymbolsInLastName() {
        CourierRegistrationRequestDTO courierRegistrationRequestDTO = new CourierRegistrationRequestDTO();
        courierRegistrationRequestDTO.setFirstName("CorrectName");
        courierRegistrationRequestDTO.setLastName("@!*%");
        assertThrows(BadRequest.class, () ->  courierService.createCourier(courierRegistrationRequestDTO));
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(courierRepository, never()).getByPhone(any());
        verify(passwordEncoder, never()).encode(any());
        verify(courierRepository, never()).save(any());
    }

    @Test
    void testCreateCourierWithTooShortLastName() {
        CourierRegistrationRequestDTO courierRegistrationRequestDTO = new CourierRegistrationRequestDTO();
        courierRegistrationRequestDTO.setFirstName("CorrectName");
        courierRegistrationRequestDTO.setLastName("c");
        assertThrows(BadRequest.class, () ->  courierService.createCourier(courierRegistrationRequestDTO));
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(courierRepository, never()).getByPhone(any());
        verify(passwordEncoder, never()).encode(any());
        verify(courierRepository, never()).save(any());
    }

    @Test
    void testCreateAlreadyExistentCourier() {
        CourierRegistrationRequestDTO courierRegistrationRequestDTO = new CourierRegistrationRequestDTO();
        courierRegistrationRequestDTO.setFirstName("CorrectName");
        courierRegistrationRequestDTO.setLastName("CorrectName");
        courierRegistrationRequestDTO.setPhone("+375333333333");
        when(courierRepository.getByPhone(any(String.class))).thenReturn(new Courier());
        assertThrows(ConflictBetweenData.class, () ->  courierService.createCourier(courierRegistrationRequestDTO));
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(courierRepository,times(1)).getByPhone(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(passwordEncoder, never()).encode(any());
        verify(courierRepository, never()).save(any());
    }

    @Test
    void testCreateCourierWithInvalidPhone() {
        CourierRegistrationRequestDTO courierRegistrationRequestDTO = new CourierRegistrationRequestDTO();
        courierRegistrationRequestDTO.setFirstName("CorrectName");
        courierRegistrationRequestDTO.setLastName("CorrectName");
        courierRegistrationRequestDTO.setPhone("wrong");
        assertThrows(BadRequest.class, () ->  courierService.createCourier(courierRegistrationRequestDTO));
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(courierRepository, times(1)).getByPhone(any());
        verify(validationService,times(1)).isPhoneCorrect(any());
        verify(passwordEncoder, never()).encode(any());
        verify(courierRepository, never()).save(any());
    }

    @Test
    void testCreateCourierWithUnconfirmedPassword() {
        CourierRegistrationRequestDTO courierRegistrationRequestDTO = new CourierRegistrationRequestDTO();
        courierRegistrationRequestDTO.setFirstName("CorrectName");
        courierRegistrationRequestDTO.setLastName("CorrectName");
        courierRegistrationRequestDTO.setPhone("+375333333333");
        courierRegistrationRequestDTO.setPassword("SomePassword");
        courierRegistrationRequestDTO.setPasswordConfirm("AnotherPassword");
        assertThrows(BadRequest.class, () ->  courierService.createCourier(courierRegistrationRequestDTO));
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(courierRepository, times(1)).getByPhone(any());
        verify(validationService,times(1)).isPhoneCorrect(any());
        verify(passwordEncoder, never()).encode(any());
        verify(courierRepository, never()).save(any());
    }

    @Test
    void testCreateCourierOk() {
        CourierRegistrationRequestDTO courierRegistrationRequestDTO = new CourierRegistrationRequestDTO();
        courierRegistrationRequestDTO.setFirstName("CorrectName");
        courierRegistrationRequestDTO.setLastName("CorrectName");
        courierRegistrationRequestDTO.setPhone("+375333333333");
        courierRegistrationRequestDTO.setPassword("SomePassword");
        courierRegistrationRequestDTO.setPasswordConfirm("SomePassword");
        courierService.createCourier(courierRegistrationRequestDTO);
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(courierRepository, times(1)).getByPhone(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(passwordEncoder, times(1)).encode(any());
        verify(courierRepository, times(1)).save(any());
    }

    @Test
    void testGetCourier() {
        Courier courier = new Courier();
        courier.setFirstName("Some name");
        when(courierRepository.existsById(any(Long.class))).thenReturn(true);
        when(courierRepository.getById(any(Long.class))).thenReturn(courier);
        CourierMainInfoDTO courierMainInfoDTO = courierService.getCourier(1);
        verify(courierRepository, times(1)).existsById(any());
        verify(courierRepository, times(2)).getById(any());
        assertEquals(courier.getFirstName(), courierMainInfoDTO.getFirstName());
    }

    @Test
    void testGetNonExistentCourier() {
        assertThrows(NotFound.class, () -> courierService.getCourier(1));
    }

    @Test
    void testGetCourierBasicInfo() {
        Courier courier = new Courier();
        courier.setFirstName("Some name");
        when(courierRepository.existsById(any(Long.class))).thenReturn(true);
        when(courierRepository.getById(any(Long.class))).thenReturn(courier);
        CourierBasicInfoDTO courierBasicInfoDTO = courierService.getCourierBasicInfo(1);
        verify(courierRepository, times(1)).existsById(any());
        verify(courierRepository, times(1)).getById(any());
        assertEquals(courier.getFirstName(), courierBasicInfoDTO.getFirstName());
    }

    @Test
    void testGetNonExistentCourierBasicInfo() {
        assertThrows(NotFound.class, () -> courierService.getCourierBasicInfo(1));
    }

    @Test
    void testGetCourierByPhoneAndPassword() {
        Courier courier = new Courier();
        courier.setFirstName("Some name");
        courier.setPassword("somePassword");
        when(courierRepository.getByPhone(any(String.class))).thenReturn(courier);
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
        CourierFullInfoDTO courierFullInfoDTO = courierService.getCourierByPhoneAndPassword("phone", "somePassword");
        verify(courierRepository, times(1)).getByPhone(any());
        verify(passwordEncoder, times(1)).matches(any(), any());
        assertEquals(courier.getFirstName(), courierFullInfoDTO.getFirstName());
        assertEquals(courier.getPassword(), courierFullInfoDTO.getPassword());
    }

    @Test
    void testGetNonExistentCourierByPhoneAndPassword() {
        assertThrows(NotFound.class, () ->  courierService.getCourierByPhoneAndPassword("phone", "somePassword"));
    }

    @Test
    void testUpdateNonExistentClient() {
        CourierMainInfoDTO courierMainInfoDTO = new CourierMainInfoDTO();
        courierMainInfoDTO.setFirstName("CorrectName");
        assertThrows(NotFound.class, () ->  courierService.updateCourier(1, courierMainInfoDTO));
        verify(courierRepository, times(1)).existsById(any());
        verify(courierRepository,  never()).getById(any());
        verify(validationService,  never()).isNameCorrect(any());
        verify(validationService,  never()).isNameLengthValid(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(courierRepository, never()).getByPhone(any());
        verify(courierRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithIncorrectSymbolsInFirstName() {
        CourierMainInfoDTO courierMainInfoDTO = new CourierMainInfoDTO();
        courierMainInfoDTO.setFirstName("@!*%");
        when(courierRepository.existsById(any(Long.class))).thenReturn(true);
        when(courierRepository.getById(any(Long.class))).thenReturn(new Courier());
        assertThrows(BadRequest.class, () -> courierService.updateCourier(1, courierMainInfoDTO));
        verify(courierRepository, times(1)).existsById(any());
        verify(courierRepository, times(1)).getById(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService,  never()).isNameLengthValid(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(courierRepository, never()).getByPhone(any());
        verify(courierRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithTooShortFirstName() {
        CourierMainInfoDTO courierMainInfoDTO = new CourierMainInfoDTO();
        courierMainInfoDTO.setFirstName("c");
        when(courierRepository.existsById(any(Long.class))).thenReturn(true);
        when(courierRepository.getById(any(Long.class))).thenReturn(new Courier());
        assertThrows(BadRequest.class, () ->courierService.updateCourier(1, courierMainInfoDTO));
        verify(courierRepository, times(1)).existsById(any());
        verify(courierRepository, times(1)).getById(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService,  times(1)).isNameLengthValid(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(courierRepository, never()).getByPhone(any());
        verify(courierRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithIncorrectSymbolsInLastName() {
        CourierMainInfoDTO courierMainInfoDTO = new CourierMainInfoDTO();
        courierMainInfoDTO.setFirstName("CorrectName");
        courierMainInfoDTO.setLastName("@!*%");
        when(courierRepository.existsById(any(Long.class))).thenReturn(true);
        when(courierRepository.getById(any(Long.class))).thenReturn(new Courier());
        assertThrows(BadRequest.class, () -> courierService.updateCourier(1, courierMainInfoDTO));
        verify(courierRepository, times(1)).existsById(any());
        verify(courierRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService,  times(1)).isNameLengthValid(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(courierRepository, never()).getByPhone(any());
        verify(courierRepository, never()).save(any());
    }

    @Test
    void testUpdateCourierWithTooShortLastName() {
        CourierMainInfoDTO courierMainInfoDTO = new CourierMainInfoDTO();
        courierMainInfoDTO.setFirstName("CorrectName");
        courierMainInfoDTO.setLastName("c");
        when(courierRepository.existsById(any(Long.class))).thenReturn(true);
        when(courierRepository.getById(any(Long.class))).thenReturn(new Courier());
        assertThrows(BadRequest.class, () -> courierService.updateCourier(1, courierMainInfoDTO));
        verify(courierRepository, times(1)).existsById(any());
        verify(courierRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService,  times(2)).isNameLengthValid(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(courierRepository, never()).getByPhone(any());
        verify(courierRepository, never()).save(any());
    }

    @Test
    void testUpdateCourierWithInvalidPhone() {
        CourierMainInfoDTO courierMainInfoDTO = new CourierMainInfoDTO();
        courierMainInfoDTO.setFirstName("CorrectName");
        courierMainInfoDTO.setLastName("CorrectName");
        courierMainInfoDTO.setPhone("wrong");
        when(courierRepository.existsById(any(Long.class))).thenReturn(true);
        when(courierRepository.getById(any(Long.class))).thenReturn(new Courier());
        assertThrows(BadRequest.class, () -> courierService.updateCourier(1, courierMainInfoDTO));
        verify(courierRepository, times(1)).existsById(any());
        verify(courierRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService,  times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(courierRepository, never()).getByPhone(any());
        verify(courierRepository, never()).save(any());
    }

    @Test
    void testUpdateCourierWithAlreadyExistentPhone() {
        CourierMainInfoDTO courierMainInfoDTO = new CourierMainInfoDTO();
        courierMainInfoDTO.setFirstName("CorrectName");
        courierMainInfoDTO.setLastName("CorrectName");
        courierMainInfoDTO.setPhone("+375333333333");
        when(courierRepository.existsById(any(Long.class))).thenReturn(true);
        when(courierRepository.getById(any(Long.class))).thenReturn(new Courier());
        when(courierRepository.getByPhone(any(String.class))).thenReturn(new Courier());
        assertThrows(ConflictBetweenData.class, () ->  courierService.updateCourier(1, courierMainInfoDTO));
        verify(courierRepository, times(1)).existsById(any());
        verify(courierRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(courierRepository, times(1)).getByPhone(any());
        verify(courierRepository, never()).save(any());
    }

    @Test
    void testUpdateCourierOk() {
        CourierMainInfoDTO courierMainInfoDTO = new CourierMainInfoDTO();
        courierMainInfoDTO.setFirstName("CorrectName");
        courierMainInfoDTO.setLastName("CorrectName");
        courierMainInfoDTO.setPhone("+375333333333");
        when(courierRepository.existsById(any(Long.class))).thenReturn(true);
        when(courierRepository.getById(any(Long.class))).thenReturn(new Courier());
        courierService.updateCourier(1, courierMainInfoDTO);
        verify(courierRepository, times(1)).existsById(any());
        verify(courierRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(courierRepository, times(1)).getByPhone(any());
        verify(courierRepository, times(1)).save(any());
    }

    @Test
    void testDeleteCourier() {
        when(courierRepository.existsById(any(Long.class))).thenReturn(true);
        when(courierRepository.getById(any(Long.class))).thenReturn(new Courier());
        courierService.deleteCourier(1);
        verify(courierRepository, times(1)).existsById(any());
        verify(courierRepository, times(1)).getById(any());
        verify(courierRepository, times(1)).deleteById(any());
    }

    @Test
    void testGetAllOrdersOfCourier() {
        Courier courier = new Courier();
        Client client = new Client();
        List<Order> orders = new ArrayList<>();
        Order correctOrder = new Order();
        correctOrder.setClient(client);
        correctOrder.setPaymentType(OrderPaymentType.BY_CARD_ONLINE);
        correctOrder.setStatus(OrderStatus.COMPLETED_LATE);
        orders.add(correctOrder);
        Order incorrectOrder = new Order();
        incorrectOrder.setStatus(OrderStatus.IN_PROCESS);
        orders.add(incorrectOrder);
        double totalOderCost = 77.7;
        when(courierRepository.existsById(any(Long.class))).thenReturn(true);
        when(courierRepository.getById(any(Long.class))).thenReturn(courier);
        when(orderRepository.getAllByCourier(any(Courier.class), any(Pageable.class))).thenReturn(orders);
        when(containerService.calculateTotalOrderCost(any(List.class))).thenReturn(totalOderCost);
        List<CourierOrderInfoDTO> courierOrderInfoDTOSList = courierService.getAllOrdersOfCourier(1);
        verify(courierRepository, times(1)).existsById(any());
        verify(courierRepository, times(1)).getById(any());
        verify(orderRepository, times(1)).getAllByCourier(any(), any());
        verify(containerService, times(1)).calculateTotalOrderCost(any());
        assertTrue(courierOrderInfoDTOSList.size() == 1);
        assertEquals(correctOrder.getPaymentType().toString().toLowerCase(), courierOrderInfoDTOSList.get(0).getPaymentType());
        assertEquals(totalOderCost, courierOrderInfoDTOSList.get(0).getOrderCost());
        assertFalse(courierOrderInfoDTOSList.get(0).isOrderDeliveredOnTime());
    }

    @Test
    void testGetAllOrdersOfCourierWithNoCompletedOrders() {
        Courier courier = new Courier();
        List<Order> orders = new ArrayList<>();
        Order incorrectOrder1 = new Order();
        incorrectOrder1.setStatus(OrderStatus.NEW);
        orders.add(incorrectOrder1);
        Order incorrectOrder2 = new Order();
        incorrectOrder2.setStatus(OrderStatus.IN_PROCESS);
        orders.add(incorrectOrder2);
        when(courierRepository.existsById(any(Long.class))).thenReturn(true);
        when(courierRepository.getById(any(Long.class))).thenReturn(courier);
        when(orderRepository.getAllByCourier(any(Courier.class), any(Pageable.class))).thenReturn(orders);
        List<CourierOrderInfoDTO> courierOrderInfoDTOSList = courierService.getAllOrdersOfCourier(1);
        verify(courierRepository, times(1)).existsById(any());
        verify(courierRepository, times(1)).getById(any());
        verify(orderRepository, times(1)).getAllByCourier(any(), any());
        assertTrue(courierOrderInfoDTOSList.isEmpty());
    }

}
