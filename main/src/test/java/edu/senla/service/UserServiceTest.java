package edu.senla.service;

import edu.senla.dao.ContainerRepository;
import edu.senla.dao.OrderRepository;
import edu.senla.dao.RoleRepository;
import edu.senla.dao.UserRepository;
import edu.senla.exeption.BadRequest;
import edu.senla.exeption.ConflictBetweenData;
import edu.senla.exeption.NotFound;
import edu.senla.model.dto.*;
import edu.senla.model.entity.Order;
import edu.senla.model.entity.Role;
import edu.senla.model.entity.User;
import edu.senla.model.enums.OrderStatus;
import edu.senla.service.impl.UserServiceImpl;
import edu.senla.service.impl.ValidationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Spy
    private ValidationServiceImpl validationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ContainerRepository containerRepository;

    @Mock
    private ContainerService containerService;

    @Spy
    private ModelMapper mapper;

    @Spy
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl clientService;

    @Test
    void testGetAllClients() {
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setFirstName("Some name");
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);
        List<UserMainInfoDTO> userMainInfoDTOS = clientService.getAllClients(10);
        verify(userRepository, times(1)).findAll();
        assertTrue(userMainInfoDTOS.size() == 1);
        assertEquals(user.getFirstName(), userMainInfoDTOS.get(0).getFirstName());
    }

    @Test
    void testGetAllClientsWhenThereAreNoClients() {
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);
        List<UserMainInfoDTO> courierMainInfoDTOs = clientService.getAllClients(10);
        verify(userRepository, times(1)).findAll();
        assertTrue(courierMainInfoDTOs.isEmpty());
    }

    @Test
    void testGetAllAdmins() {
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        List<User> userList = new ArrayList<>();
        User userAdmin = new User();
        userAdmin.setFirstName("Admin name");
        userAdmin.getRoles().add(adminRole);
        userList.add(userAdmin);
        when(roleRepository.getByName(any(String.class))).thenReturn(adminRole);
        when(userRepository.getAllByRoles(any(Role.class))).thenReturn(userList);
        List<AdminInfoDTO> adminInfoDTOs = clientService.getAllAdmins(10);
        verify(roleRepository, times(1)).getByName(any());
        verify(userRepository, times(1)).getAllByRoles(any());
        assertTrue(adminInfoDTOs.size() == 1);
        assertEquals(userAdmin.getFirstName(), adminInfoDTOs.get(0).getFirstName());
    }

    @Test
    void testGetAllAdminsWhenThereAreNoAdmins() {
        when(roleRepository.getByName(any(String.class))).thenReturn(new Role());
        List<User> userList = new ArrayList<>();
        when(userRepository.getAllByRoles(any(Role.class))).thenReturn(userList);
        List<AdminInfoDTO> adminInfoDTOs = clientService.getAllAdmins(10);
        verify(userRepository, times(1)).getAllByRoles(any());
        assertTrue(adminInfoDTOs.isEmpty());
    }

    @Test
    void testGetAllOrdersOfClientWhenThereIsNoCompletedOrders() {
        User user = new User();
        user.setFirstName("Some name");
        List<Order> orders = new ArrayList<>();
        Order incorrectOrder1 = new Order();
        incorrectOrder1.setStatus(OrderStatus.NEW);
        orders.add(incorrectOrder1);
        Order incorrectOrder2 = new Order();
        incorrectOrder2.setStatus(OrderStatus.IN_PROCESS);
        orders.add(incorrectOrder2);
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        when(userRepository.getById(any(Long.class))).thenReturn(user);
        when(orderRepository.getAllByUser(any(User.class))).thenReturn(orders);
        List<UserOrderInfoDTO> userOrderInfoDTOS = clientService.getAllOrdersOfClient(1);
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, times(1)).getById(any());
        verify(orderRepository, times(1)).getAllByUser(any());
        assertTrue(userOrderInfoDTOS.isEmpty());
    }

    @Test
    void testCreateClientWithIncorrectSymbolsInFirstName() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("@!*%");
        assertThrows(BadRequest.class, () ->  clientService.createClient(new String()));
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).getByPhone(any());
        verify(userRepository, never()).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateClientWithTooShortFirstName() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("c");
        assertThrows(BadRequest.class, () ->  clientService.createClient(new String()));
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).getByPhone(any());
        verify(userRepository, never()).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateClientWithIncorrectSymbolsInLastName() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("CorrectName");
        newRegistrationRequestDTO.setLastName("@!*%");
        assertThrows(BadRequest.class, () ->  clientService.createClient(new String()));
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).getByPhone(any());
        verify(userRepository, never()).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateClientWithTooShortLastName() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("CorrectName");
        newRegistrationRequestDTO.setLastName("c");
        assertThrows(BadRequest.class, () ->  clientService.createClient(new String()));
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).getByPhone(any());
        verify(userRepository, never()).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateClientWithInvalidEmail() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("CorrectName");
        newRegistrationRequestDTO.setLastName("CorrectName");
        newRegistrationRequestDTO.setEmail("wrong");
        assertThrows(BadRequest.class, () ->  clientService.createClient(new String()));
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).getByPhone(any());
        verify(userRepository, never()).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateClientWithInvalidPhone() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("CorrectName");
        newRegistrationRequestDTO.setLastName("CorrectName");
        newRegistrationRequestDTO.setEmail("test@test.com");
        newRegistrationRequestDTO.setPhone("wrong");
        assertThrows(BadRequest.class, () ->  clientService.createClient(new String()));
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).getByPhone(any());
        verify(userRepository, never()).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateClientWithAlreadyExistentEmail() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("CorrectName");
        newRegistrationRequestDTO.setLastName("CorrectName");
        newRegistrationRequestDTO.setEmail("test@test.com");
        newRegistrationRequestDTO.setPhone("+375333333333");
        when(userRepository.getByEmail(any(String.class))).thenReturn(new User());
        assertThrows(ConflictBetweenData.class, () ->  clientService.createClient(new String()));
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(userRepository, times(1)).getByEmail(any());;
        verify(userRepository, never()).getByPhone(any());
        verify(userRepository, never()).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateClientWithAlreadyExistentPhone() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("CorrectName");
        newRegistrationRequestDTO.setLastName("CorrectName");
        newRegistrationRequestDTO.setEmail("test@test.com");
        newRegistrationRequestDTO.setPhone("+375333333333");
        when(userRepository.getByPhone(any(String.class))).thenReturn(new User());
        assertThrows(ConflictBetweenData.class, () ->  clientService.createClient(new String()));
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(userRepository, times(1)).getByEmail(any());;
        verify(userRepository, times(1)).getByPhone(any());
        verify(userRepository, never()).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateClientWithAlreadyExistentUsername() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("CorrectName");
        newRegistrationRequestDTO.setLastName("CorrectName");
        newRegistrationRequestDTO.setEmail("test@test.com");
        newRegistrationRequestDTO.setPhone("+375333333333");
        newRegistrationRequestDTO.setUsername("Username");
        when(userRepository.getByUsername(any(String.class))).thenReturn(new User());
        assertThrows(ConflictBetweenData.class, () ->  clientService.createClient(new String()));
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(userRepository, times(1)).getByEmail(any());;
        verify(userRepository, times(1)).getByPhone(any());
        verify(userRepository, times(1)).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateClientWithUnconfirmedPassword() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("CorrectName");
        newRegistrationRequestDTO.setLastName("CorrectName");
        newRegistrationRequestDTO.setEmail("test@test.com");
        newRegistrationRequestDTO.setPhone("+375333333333");
        newRegistrationRequestDTO.setUsername("Username");
        newRegistrationRequestDTO.setPassword("testPassword");
        newRegistrationRequestDTO.setPasswordConfirm("AnotherTestPassword");
        assertThrows(BadRequest.class, () ->  clientService.createClient(new String()));
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(userRepository, times(1)).getByEmail(any());;
        verify(userRepository, times(1)).getByPhone(any());
        verify(userRepository, times(1)).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateClientOk() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("CorrectName");
        newRegistrationRequestDTO.setLastName("CorrectName");
        newRegistrationRequestDTO.setEmail("test@test.com");
        newRegistrationRequestDTO.setPhone("+375333333333");
        newRegistrationRequestDTO.setUsername("Username");
        newRegistrationRequestDTO.setPassword("testPassword");
        newRegistrationRequestDTO.setPasswordConfirm("testPassword");
        clientService.createClient(new String());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(userRepository, times(1)).getByEmail(any());;
        verify(userRepository, times(1)).getByPhone(any());
        verify(userRepository, times(1)).getByUsername(any());
        verify(roleRepository, times(1)).getByName(any());
        verify(passwordEncoder, times(1)).encode(any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testGetClient() {
        User user = new User();
        user.setFirstName("Some name");
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        when(userRepository.getById(any(Long.class))).thenReturn(user);
        UserMainInfoDTO userMainInfoDTO = clientService.getClient(1);
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, times(2)).getById(any());
        assertEquals(user.getFirstName(), userMainInfoDTO.getFirstName());
    }

    @Test
    void testGetClientByUsernameAndPassword() {
        User user = new User();
        user.setFirstName("Some name");
        user.setPassword("somePassword");
        when(userRepository.getByUsername(any(String.class))).thenReturn(user);
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
        UserFullInfoDTO userFullInfoDTO = clientService.getClientByUsernameAndPassword(new String());
        verify(userRepository, times(1)).getByUsername(any());
        verify(passwordEncoder, times(1)).matches(any(), any());
        assertEquals(user.getFirstName(), userFullInfoDTO.getFirstName());
        assertEquals(user.getPassword(), userFullInfoDTO.getPassword());
    }

    @Test
    void testGetClientByUsernameAndPasswordWithIncorrectPassword() {
        User user = new User();
        user.setFirstName("Some name");
        user.setPassword("somePassword");
        when(userRepository.getByUsername(any(String.class))).thenReturn(user);
        assertThrows(NotFound.class, () -> clientService.getClientByUsernameAndPassword(new String()));
        verify(userRepository, times(1)).getByUsername(any());
        verify(passwordEncoder, times(1)).matches(any(), any());
    }

    @Test
    void testGetNonExistentClientByUsernameAndPassword() {
        assertThrows(NotFound.class, () -> clientService.getClientByUsernameAndPassword(new String()));
    }

    @Test
    void testUpdateNonExistentClient() {
        UserMainInfoDTO userMainInfoDTO = new UserMainInfoDTO();
        userMainInfoDTO.setFirstName("CorrectName");
        assertThrows(NotFound.class, () ->  clientService.updateClient(1, new String()));
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, never()).getById(any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).getByPhone(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithIncorrectSymbolsInFirstName() {
        UserMainInfoDTO userMainInfoDTO = new UserMainInfoDTO();
        userMainInfoDTO.setFirstName("@!*%");
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        when(userRepository.getById(any(Long.class))).thenReturn(new User());
        assertThrows(BadRequest.class, () ->  clientService.updateClient(1, new String()));
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, times(1)).getById(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).getByPhone(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithTooShortFirstName() {
        UserMainInfoDTO userMainInfoDTO = new UserMainInfoDTO();
        userMainInfoDTO.setFirstName("c");
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        when(userRepository.getById(any(Long.class))).thenReturn(new User());
        assertThrows(BadRequest.class, () ->  clientService.updateClient(1, new String()));
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, times(1)).getById(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService,  times(1)).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).getByPhone(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithIncorrectSymbolsInLastName() {
        UserMainInfoDTO userMainInfoDTO = new UserMainInfoDTO();
        userMainInfoDTO.setFirstName("CorrectName");
        userMainInfoDTO.setLastName("@!*%");
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        when(userRepository.getById(any(Long.class))).thenReturn(new User());
        assertThrows(BadRequest.class, () ->  clientService.updateClient(1, new String()));
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService,  times(1)).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).getByPhone(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithTooShortLastName() {
        UserMainInfoDTO userMainInfoDTO = new UserMainInfoDTO();
        userMainInfoDTO.setFirstName("CorrectName");
        userMainInfoDTO.setLastName("c");
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        when(userRepository.getById(any(Long.class))).thenReturn(new User());
        assertThrows(BadRequest.class, () ->  clientService.updateClient(1, new String()));
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService,  times(2)).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).getByPhone(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithInvalidEmail() {
        UserMainInfoDTO userMainInfoDTO = new UserMainInfoDTO();
        userMainInfoDTO.setFirstName("CorrectName");
        userMainInfoDTO.setLastName("CorrectName");
        userMainInfoDTO.setEmail("wrong");
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        when(userRepository.getById(any(Long.class))).thenReturn(new User());
        assertThrows(BadRequest.class, () ->  clientService.updateClient(1, new String()));
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService,  times(2)).isNameLengthValid(any());
        verify(validationService,  times(1)).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).getByPhone(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithInvalidPhone() {
        UserMainInfoDTO userMainInfoDTO = new UserMainInfoDTO();
        userMainInfoDTO.setFirstName("CorrectName");
        userMainInfoDTO.setLastName("CorrectName");
        userMainInfoDTO.setEmail("test@test.com");
        userMainInfoDTO.setPhone("wrong");
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        when(userRepository.getById(any(Long.class))).thenReturn(new User());
        assertThrows(BadRequest.class, () ->  clientService.updateClient(1, new String()));
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService,  times(2)).isNameLengthValid(any());
        verify(validationService,  times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).getByPhone(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithAlreadyExistentEmail() {
        UserMainInfoDTO userMainInfoDTO = new UserMainInfoDTO();
        userMainInfoDTO.setFirstName("CorrectName");
        userMainInfoDTO.setLastName("CorrectName");
        userMainInfoDTO.setEmail("test@test.com");
        userMainInfoDTO.setPhone("+375333333333");
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        when(userRepository.getById(any(Long.class))).thenReturn(new User());
        when(userRepository.getByEmail(any(String.class))).thenReturn(new User());
        assertThrows(ConflictBetweenData.class, () ->  clientService.updateClient(1, new String()));
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService,  times(2)).isNameLengthValid(any());
        verify(validationService,  times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(userRepository, times(1)).getByEmail(any());
        verify(userRepository, never()).getByPhone(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithAlreadyExistentPhone() {
        UserMainInfoDTO userMainInfoDTO = new UserMainInfoDTO();
        userMainInfoDTO.setFirstName("CorrectName");
        userMainInfoDTO.setLastName("CorrectName");
        userMainInfoDTO.setEmail("test@test.com");
        userMainInfoDTO.setPhone("+375333333333");
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        when(userRepository.getById(any(Long.class))).thenReturn(new User());
        when(userRepository.getByPhone(any(String.class))).thenReturn(new User());
        assertThrows(ConflictBetweenData.class, () ->  clientService.updateClient(1, new String()));
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService,  times(2)).isNameLengthValid(any());
        verify(validationService,  times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(userRepository, times(1)).getByEmail(any());
        verify(userRepository, times(1)).getByPhone(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateClientOk() {
        UserMainInfoDTO userMainInfoDTO = new UserMainInfoDTO();
        userMainInfoDTO.setFirstName("CorrectName");
        userMainInfoDTO.setLastName("CorrectName");
        userMainInfoDTO.setEmail("test@test.com");
        userMainInfoDTO.setPhone("+375333333333");
        userMainInfoDTO.setAddress("address");
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        when(userRepository.getById(any(Long.class))).thenReturn(new User());
        clientService.updateClient(1, new String());
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(userRepository, times(1)).getByEmail(any());;
        verify(userRepository, times(1)).getByPhone(any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testDeleteClient() {
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        when(userRepository.getById(any(Long.class))).thenReturn(new User());
        clientService.deleteClient(1);
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, times(1)).getById(any());
        verify(userRepository, times(1)).deleteById(any());
    }

    @Test
    void testGrantAdministratorRightsWhenUserIsAlreadyAdmin() {
        Role role = new Role();
        User user = new User();
        user.setFirstName("SomeName");
        role.setName("ROLE_ADMIN");
        user.getRoles().add(role);
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        when(userRepository.getById(any(Long.class))).thenReturn(user);
        when(roleRepository.getByName(any(String.class))).thenReturn(role);
        assertThrows(BadRequest.class, () -> clientService.grantAdministratorRights(1));
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, times(1)).getById(any());
        verify(roleRepository, times(1)).getByName(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testGrantAdministratorRightsWhenUserIsNonExistent() {
        assertThrows(NotFound.class, () -> clientService.grantAdministratorRights(1));
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, never()).getById(any());
        verify(roleRepository, never()).getByName(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testGrantAdministratorRightOk() {
        Role role = new Role();
        User user = new User();
        user.setFirstName("SomeName");
        role.setName("ROLE_USER");
        user.getRoles().add(role);
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        when(userRepository.getById(any(Long.class))).thenReturn(user);
        clientService.grantAdministratorRights(1);
        verify(userRepository, times(2)).existsById(any());
        verify(userRepository, times(2)).getById(any());
        verify(roleRepository, times(2)).getByName(any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testRevokeAdministratorRightsWhenUserIsAlreadyUser() {
        Role role = new Role();
        User user = new User();
        user.setFirstName("SomeName");
        role.setName("ROLE_ADMIN");
        user.getRoles().add(role);
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        when(userRepository.getById(any(Long.class))).thenReturn(user);
        assertThrows(BadRequest.class, () -> clientService.revokeAdministratorRights(1));
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, times(1)).getById(any());
        verify(roleRepository, times(1)).getByName(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRevokeAdministratorRightsWhenUserIsNonExistent() {
        assertThrows(NotFound.class, () -> clientService.revokeAdministratorRights(1));
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, never()).getById(any());
        verify(roleRepository, never()).getByName(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRevokeAdministratorRightsOk() {
        Role role = new Role();
        User user = new User();
        user.setFirstName("SomeName");
        role.setName("ROLE_ADMIN");
        user.getRoles().add(role);
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        when(userRepository.getById(any(Long.class))).thenReturn(user);
        when(roleRepository.getByName(any(String.class))).thenReturn(role);
        clientService.revokeAdministratorRights(1);
        verify(userRepository, times(2)).existsById(any());
        verify(userRepository, times(2)).getById(any());
        verify(roleRepository, times(2)).getByName(any());
        verify(userRepository, times(1)).save(any());
    }

}

