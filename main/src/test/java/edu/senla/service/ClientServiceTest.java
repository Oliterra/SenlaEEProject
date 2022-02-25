package edu.senla.service;

<<<<<<< Updated upstream:main/src/test/java/edu/senla/service/ClientServiceTest.java
import edu.senla.dao.ClientRepository;
=======
>>>>>>> Stashed changes:main/src/test/java/edu/senla/service/UserServiceTest.java
import edu.senla.dao.ContainerRepository;
import edu.senla.dao.OrderRepository;
import edu.senla.dao.RoleRepository;
import edu.senla.dao.UserRepository;
import edu.senla.exeption.BadRequest;
import edu.senla.exeption.ConflictBetweenData;
import edu.senla.exeption.NotFound;
import edu.senla.model.dto.*;
<<<<<<< Updated upstream:main/src/test/java/edu/senla/service/ClientServiceTest.java
import edu.senla.model.entity.Client;
=======
>>>>>>> Stashed changes:main/src/test/java/edu/senla/service/UserServiceTest.java
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
public class ClientServiceTest {

    @Spy
    private ValidationServiceImpl validationService;

    @Mock
    private ClientRepository clientRepository;

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

    /*@Test
    void testGetAllClients() {
<<<<<<< Updated upstream:main/src/test/java/edu/senla/service/ClientServiceTest.java
        List<Client> clientList = new ArrayList<>();
        Client client = new Client();
        client.setFirstName("Some name");
        clientList.add(client);
        Page<Client> clients = new PageImpl<>(clientList);
        when(clientRepository.findAll(any(Pageable.class))).thenReturn(clients);
        List<ClientMainInfoDTO> clientMainInfoDTOs = clientService.getAllClients(10);
        verify(clientRepository, times(1)).findAll((Pageable) any());
        assertTrue(clientMainInfoDTOs.size() == 1);
        assertEquals(client.getFirstName(), clientMainInfoDTOs.get(0).getFirstName());
=======
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setFirstName("Some name");
        userList.add(user);
        Page<User> clients = new PageImpl<>(userList);
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(clients);
        List<UserMainInfoDTO> userMainInfoDTOS = clientService.getAllClients(10);
        verify(userRepository, times(1)).findAll((PageRequest) any());
        assertTrue(userMainInfoDTOS.size() == 1);
        assertEquals(user.getFirstName(), userMainInfoDTOS.get(0).getFirstName());
>>>>>>> Stashed changes:main/src/test/java/edu/senla/service/UserServiceTest.java
    }

    @Test
    void testGetAllClientsWhenThereAreNoClients() {
<<<<<<< Updated upstream:main/src/test/java/edu/senla/service/ClientServiceTest.java
        List<Client> clientList = new ArrayList<>();
        Page<Client> clients = new PageImpl<>(clientList);
        when(clientRepository.findAll(any(Pageable.class))).thenReturn(clients);
        List<ClientMainInfoDTO> courierMainInfoDTOs = clientService.getAllClients(10);
        verify(clientRepository, times(1)).findAll((Pageable) any());
=======
        List<User> userList = new ArrayList<>();
        Page<User> clients = new PageImpl<>(userList);
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(clients);
        List<UserMainInfoDTO> courierMainInfoDTOs = clientService.getAllClients(10);
        verify(userRepository, times(1)).findAll((PageRequest) any());
>>>>>>> Stashed changes:main/src/test/java/edu/senla/service/UserServiceTest.java
        assertTrue(courierMainInfoDTOs.isEmpty());
    }*/

    @Test
    void testGetAllAdmins() {
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        List<Client> clientList = new ArrayList<>();
        Client clientAdmin = new Client();
        clientAdmin.setFirstName("Admin name");
        clientAdmin.setRole(adminRole);
        clientList.add(clientAdmin);
        when(roleRepository.getByName(any(String.class))).thenReturn(adminRole);
<<<<<<< Updated upstream:main/src/test/java/edu/senla/service/ClientServiceTest.java
        when(clientRepository.getAllByRole(any(Role.class), any(Pageable.class))).thenReturn(clientList);
        List<AdminInfoDTO> adminInfoDTOs = clientService.getAllAdmins(10);
        verify(roleRepository, times(1)).getByName(any());
        verify(clientRepository, times(1)).getAllByRole(any(), any());
=======
        when(userRepository.getAllByRoles(any(Role.class))).thenReturn(userList);
        List<AdminInfoDTO> adminInfoDTOs = clientService.getAllAdmins(10);
        verify(roleRepository, times(1)).getByName(any());
        verify(userRepository, times(1)).getAllByRoles(any());
>>>>>>> Stashed changes:main/src/test/java/edu/senla/service/UserServiceTest.java
        assertTrue(adminInfoDTOs.size() == 1);
        assertEquals(clientAdmin.getFirstName(), adminInfoDTOs.get(0).getFirstName());
    }

    @Test
    void testGetAllAdminsWhenThereAreNoAdmins() {
        when(roleRepository.getByName(any(String.class))).thenReturn(new Role());
<<<<<<< Updated upstream:main/src/test/java/edu/senla/service/ClientServiceTest.java
        List<Client> clientList = new ArrayList<>();
        when(clientRepository.getAllByRole(any(Role.class), any(Pageable.class))).thenReturn(clientList);
        List<AdminInfoDTO> adminInfoDTOs = clientService.getAllAdmins(10);
        verify(clientRepository, times(1)).getAllByRole(any(), any());
=======
        List<User> userList = new ArrayList<>();
        when(userRepository.getAllByRoles(any(Role.class))).thenReturn(userList);
        List<AdminInfoDTO> adminInfoDTOs = clientService.getAllAdmins(10);
        verify(userRepository, times(1)).getAllByRoles(any());
>>>>>>> Stashed changes:main/src/test/java/edu/senla/service/UserServiceTest.java
        assertTrue(adminInfoDTOs.isEmpty());
    }

    @Test
    void testGetAllOrdersOfClientWhenThereIsNoCompletedOrders() {
        Client client = new Client();
        client.setFirstName("Some name");
        List<Order> orders = new ArrayList<>();
        Order incorrectOrder1 = new Order();
        incorrectOrder1.setStatus(OrderStatus.NEW);
        orders.add(incorrectOrder1);
        Order incorrectOrder2 = new Order();
        incorrectOrder2.setStatus(OrderStatus.IN_PROCESS);
        orders.add(incorrectOrder2);
<<<<<<< Updated upstream:main/src/test/java/edu/senla/service/ClientServiceTest.java
        when(clientRepository.existsById(any(Long.class))).thenReturn(true);
        when(clientRepository.getById(any(Long.class))).thenReturn(client);
        when(orderRepository.getAllByClient(any(Client.class), any(Pageable.class))).thenReturn(orders);
        List<ClientOrderInfoDTO> clientOrderInfoDTOs = clientService.getAllOrdersOfClient(1);
        verify(clientRepository, times(1)).existsById(any());
        verify(clientRepository, times(1)).getById(any());
        verify(orderRepository, times(1)).getAllByClient(any(), any());
        assertTrue(clientOrderInfoDTOs.isEmpty());
=======
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        when(userRepository.getById(any(Long.class))).thenReturn(user);
        when(orderRepository.getAllByUser(any(User.class))).thenReturn(orders);
        List<UserOrderInfoDTO> userOrderInfoDTOS = clientService.getAllOrdersOfClient(1);
        verify(userRepository, times(1)).existsById(any());
        verify(userRepository, times(1)).getById(any());
        verify(orderRepository, times(1)).getAllByUser(any());
        assertTrue(userOrderInfoDTOS.isEmpty());
>>>>>>> Stashed changes:main/src/test/java/edu/senla/service/UserServiceTest.java
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
        verify(clientRepository, never()).getByEmail(any());
        verify(clientRepository, never()).getByPhone(any());
        verify(clientRepository, never()).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(clientRepository, never()).save(any());
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
        verify(clientRepository, never()).getByEmail(any());
        verify(clientRepository, never()).getByPhone(any());
        verify(clientRepository, never()).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(clientRepository, never()).save(any());
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
        verify(clientRepository, never()).getByEmail(any());
        verify(clientRepository, never()).getByPhone(any());
        verify(clientRepository, never()).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(clientRepository, never()).save(any());
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
        verify(clientRepository, never()).getByEmail(any());
        verify(clientRepository, never()).getByPhone(any());
        verify(clientRepository, never()).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(clientRepository, never()).save(any());
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
        verify(clientRepository, never()).getByEmail(any());
        verify(clientRepository, never()).getByPhone(any());
        verify(clientRepository, never()).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(clientRepository, never()).save(any());
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
        verify(clientRepository, never()).getByEmail(any());
        verify(clientRepository, never()).getByPhone(any());
        verify(clientRepository, never()).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testCreateClientWithAlreadyExistentEmail() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("CorrectName");
        newRegistrationRequestDTO.setLastName("CorrectName");
        newRegistrationRequestDTO.setEmail("test@test.com");
        newRegistrationRequestDTO.setPhone("+375333333333");
        when(clientRepository.getByEmail(any(String.class))).thenReturn(new Client());
        assertThrows(ConflictBetweenData.class, () ->  clientService.createClient(new String()));
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(clientRepository, times(1)).getByEmail(any());;
        verify(clientRepository, never()).getByPhone(any());
        verify(clientRepository, never()).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testCreateClientWithAlreadyExistentPhone() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("CorrectName");
        newRegistrationRequestDTO.setLastName("CorrectName");
        newRegistrationRequestDTO.setEmail("test@test.com");
        newRegistrationRequestDTO.setPhone("+375333333333");
        when(clientRepository.getByPhone(any(String.class))).thenReturn(new Client());
        assertThrows(ConflictBetweenData.class, () ->  clientService.createClient(new String()));
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(clientRepository, times(1)).getByEmail(any());;
        verify(clientRepository, times(1)).getByPhone(any());
        verify(clientRepository, never()).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testCreateClientWithAlreadyExistentUsername() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("CorrectName");
        newRegistrationRequestDTO.setLastName("CorrectName");
        newRegistrationRequestDTO.setEmail("test@test.com");
        newRegistrationRequestDTO.setPhone("+375333333333");
        newRegistrationRequestDTO.setUsername("Username");
        when(clientRepository.getByUsername(any(String.class))).thenReturn(new Client());
        assertThrows(ConflictBetweenData.class, () ->  clientService.createClient(new String()));
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(clientRepository, times(1)).getByEmail(any());;
        verify(clientRepository, times(1)).getByPhone(any());
        verify(clientRepository, times(1)).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(clientRepository, never()).save(any());
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
        verify(clientRepository, times(1)).getByEmail(any());;
        verify(clientRepository, times(1)).getByPhone(any());
        verify(clientRepository, times(1)).getByUsername(any());
        verify(roleRepository, never()).getByName(any());
        verify(passwordEncoder, never()).encode(any());
        verify(clientRepository, never()).save(any());
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
        verify(clientRepository, times(1)).getByEmail(any());;
        verify(clientRepository, times(1)).getByPhone(any());
        verify(clientRepository, times(1)).getByUsername(any());
        verify(roleRepository, times(1)).getByName(any());
        verify(passwordEncoder, times(1)).encode(any());
        verify(clientRepository, times(1)).save(any());
    }

    @Test
    void testGetClient() {
        Client client = new Client();
        client.setFirstName("Some name");
        when(clientRepository.existsById(any(Long.class))).thenReturn(true);
        when(clientRepository.getById(any(Long.class))).thenReturn(client);
        ClientMainInfoDTO clientMainInfoDTO = clientService.getClient(1);
        verify(clientRepository, times(1)).existsById(any());
        verify(clientRepository, times(2)).getById(any());
        assertEquals(client.getFirstName(), clientMainInfoDTO.getFirstName());
    }

    @Test
    void testGetClientByUsernameAndPassword() {
        Client client = new Client();
        client.setFirstName("Some name");
        client.setPassword("somePassword");
        when(clientRepository.getByUsername(any(String.class))).thenReturn(client);
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
        ClientFullInfoDTO clientFullInfoDTO = clientService.getClientByUsernameAndPassword(new String());
        verify(clientRepository, times(1)).getByUsername(any());
        verify(passwordEncoder, times(1)).matches(any(), any());
        assertEquals(client.getFirstName(), clientFullInfoDTO.getFirstName());
        assertEquals(client.getPassword(), clientFullInfoDTO.getPassword());
    }

    @Test
    void testGetClientByUsernameAndPasswordWithIncorrectPassword() {
        Client client = new Client();
        client.setFirstName("Some name");
        client.setPassword("somePassword");
        when(clientRepository.getByUsername(any(String.class))).thenReturn(client);
        assertThrows(NotFound.class, () -> clientService.getClientByUsernameAndPassword(new String()));
        verify(clientRepository, times(1)).getByUsername(any());
        verify(passwordEncoder, times(1)).matches(any(), any());
    }

    @Test
    void testGetNonExistentClientByUsernameAndPassword() {
        assertThrows(NotFound.class, () -> clientService.getClientByUsernameAndPassword(new String()));
    }

    @Test
    void testUpdateNonExistentClient() {
        ClientMainInfoDTO clientMainInfoDTO = new ClientMainInfoDTO();
        clientMainInfoDTO.setFirstName("CorrectName");
        assertThrows(NotFound.class, () ->  clientService.updateClient(1, new String()));
        verify(clientRepository, times(1)).existsById(any());
        verify(clientRepository, never()).getById(any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(clientRepository, never()).getByEmail(any());
        verify(clientRepository, never()).getByPhone(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithIncorrectSymbolsInFirstName() {
        ClientMainInfoDTO clientMainInfoDTO = new ClientMainInfoDTO();
        clientMainInfoDTO.setFirstName("@!*%");
        when(clientRepository.existsById(any(Long.class))).thenReturn(true);
        when(clientRepository.getById(any(Long.class))).thenReturn(new Client());
        assertThrows(BadRequest.class, () ->  clientService.updateClient(1, new String()));
        verify(clientRepository, times(1)).existsById(any());
        verify(clientRepository, times(1)).getById(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(clientRepository, never()).getByEmail(any());
        verify(clientRepository, never()).getByPhone(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithTooShortFirstName() {
        ClientMainInfoDTO clientMainInfoDTO = new ClientMainInfoDTO();
        clientMainInfoDTO.setFirstName("c");
        when(clientRepository.existsById(any(Long.class))).thenReturn(true);
        when(clientRepository.getById(any(Long.class))).thenReturn(new Client());
        assertThrows(BadRequest.class, () ->  clientService.updateClient(1, new String()));
        verify(clientRepository, times(1)).existsById(any());
        verify(clientRepository, times(1)).getById(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService,  times(1)).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(clientRepository, never()).getByEmail(any());
        verify(clientRepository, never()).getByPhone(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithIncorrectSymbolsInLastName() {
        ClientMainInfoDTO clientMainInfoDTO = new ClientMainInfoDTO();
        clientMainInfoDTO.setFirstName("CorrectName");
        clientMainInfoDTO.setLastName("@!*%");
        when(clientRepository.existsById(any(Long.class))).thenReturn(true);
        when(clientRepository.getById(any(Long.class))).thenReturn(new Client());
        assertThrows(BadRequest.class, () ->  clientService.updateClient(1, new String()));
        verify(clientRepository, times(1)).existsById(any());
        verify(clientRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService,  times(1)).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(clientRepository, never()).getByEmail(any());
        verify(clientRepository, never()).getByPhone(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithTooShortLastName() {
        ClientMainInfoDTO clientMainInfoDTO = new ClientMainInfoDTO();
        clientMainInfoDTO.setFirstName("CorrectName");
        clientMainInfoDTO.setLastName("c");
        when(clientRepository.existsById(any(Long.class))).thenReturn(true);
        when(clientRepository.getById(any(Long.class))).thenReturn(new Client());
        assertThrows(BadRequest.class, () ->  clientService.updateClient(1, new String()));
        verify(clientRepository, times(1)).existsById(any());
        verify(clientRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService,  times(2)).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(clientRepository, never()).getByEmail(any());
        verify(clientRepository, never()).getByPhone(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithInvalidEmail() {
        ClientMainInfoDTO clientMainInfoDTO = new ClientMainInfoDTO();
        clientMainInfoDTO.setFirstName("CorrectName");
        clientMainInfoDTO.setLastName("CorrectName");
        clientMainInfoDTO.setEmail("wrong");
        when(clientRepository.existsById(any(Long.class))).thenReturn(true);
        when(clientRepository.getById(any(Long.class))).thenReturn(new Client());
        assertThrows(BadRequest.class, () ->  clientService.updateClient(1, new String()));
        verify(clientRepository, times(1)).existsById(any());
        verify(clientRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService,  times(2)).isNameLengthValid(any());
        verify(validationService,  times(1)).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
        verify(clientRepository, never()).getByEmail(any());
        verify(clientRepository, never()).getByPhone(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithInvalidPhone() {
        ClientMainInfoDTO clientMainInfoDTO = new ClientMainInfoDTO();
        clientMainInfoDTO.setFirstName("CorrectName");
        clientMainInfoDTO.setLastName("CorrectName");
        clientMainInfoDTO.setEmail("test@test.com");
        clientMainInfoDTO.setPhone("wrong");
        when(clientRepository.existsById(any(Long.class))).thenReturn(true);
        when(clientRepository.getById(any(Long.class))).thenReturn(new Client());
        assertThrows(BadRequest.class, () ->  clientService.updateClient(1, new String()));
        verify(clientRepository, times(1)).existsById(any());
        verify(clientRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService,  times(2)).isNameLengthValid(any());
        verify(validationService,  times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(clientRepository, never()).getByEmail(any());
        verify(clientRepository, never()).getByPhone(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithAlreadyExistentEmail() {
        ClientMainInfoDTO clientMainInfoDTO = new ClientMainInfoDTO();
        clientMainInfoDTO.setFirstName("CorrectName");
        clientMainInfoDTO.setLastName("CorrectName");
        clientMainInfoDTO.setEmail("test@test.com");
        clientMainInfoDTO.setPhone("+375333333333");
        when(clientRepository.existsById(any(Long.class))).thenReturn(true);
        when(clientRepository.getById(any(Long.class))).thenReturn(new Client());
        when(clientRepository.getByEmail(any(String.class))).thenReturn(new Client());
        assertThrows(ConflictBetweenData.class, () ->  clientService.updateClient(1, new String()));
        verify(clientRepository, times(1)).existsById(any());
        verify(clientRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService,  times(2)).isNameLengthValid(any());
        verify(validationService,  times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(clientRepository, times(1)).getByEmail(any());
        verify(clientRepository, never()).getByPhone(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testUpdateClientWithAlreadyExistentPhone() {
        ClientMainInfoDTO clientMainInfoDTO = new ClientMainInfoDTO();
        clientMainInfoDTO.setFirstName("CorrectName");
        clientMainInfoDTO.setLastName("CorrectName");
        clientMainInfoDTO.setEmail("test@test.com");
        clientMainInfoDTO.setPhone("+375333333333");
        when(clientRepository.existsById(any(Long.class))).thenReturn(true);
        when(clientRepository.getById(any(Long.class))).thenReturn(new Client());
        when(clientRepository.getByPhone(any(String.class))).thenReturn(new Client());
        assertThrows(ConflictBetweenData.class, () ->  clientService.updateClient(1, new String()));
        verify(clientRepository, times(1)).existsById(any());
        verify(clientRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService,  times(2)).isNameLengthValid(any());
        verify(validationService,  times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(clientRepository, times(1)).getByEmail(any());
        verify(clientRepository, times(1)).getByPhone(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testUpdateClientOk() {
        ClientMainInfoDTO clientMainInfoDTO = new ClientMainInfoDTO();
        clientMainInfoDTO.setFirstName("CorrectName");
        clientMainInfoDTO.setLastName("CorrectName");
        clientMainInfoDTO.setEmail("test@test.com");
        clientMainInfoDTO.setPhone("+375333333333");
        clientMainInfoDTO.setAddress("address");
        when(clientRepository.existsById(any(Long.class))).thenReturn(true);
        when(clientRepository.getById(any(Long.class))).thenReturn(new Client());
        clientService.updateClient(1, new String());
        verify(clientRepository, times(1)).existsById(any());
        verify(clientRepository, times(1)).getById(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
        verify(clientRepository, times(1)).getByEmail(any());;
        verify(clientRepository, times(1)).getByPhone(any());
        verify(clientRepository, times(1)).save(any());
    }

    @Test
    void testDeleteClient() {
        when(clientRepository.existsById(any(Long.class))).thenReturn(true);
        when(clientRepository.getById(any(Long.class))).thenReturn(new Client());
        clientService.deleteClient(1);
        verify(clientRepository, times(1)).existsById(any());
        verify(clientRepository, times(1)).getById(any());
        verify(clientRepository, times(1)).deleteById(any());
    }

    @Test
    void testGrantAdministratorRightsWhenUserIsAlreadyAdmin() {
        Role role = new Role();
        Client client = new Client();
        client.setFirstName("SomeName");
        role.setName("ROLE_ADMIN");
        client.setRole(role);
        when(clientRepository.existsById(any(Long.class))).thenReturn(true);
        when(clientRepository.getById(any(Long.class))).thenReturn(client);
        when(roleRepository.getByName(any(String.class))).thenReturn(role);
        assertThrows(BadRequest.class, () -> clientService.grantAdministratorRights(1));
        verify(clientRepository, times(1)).existsById(any());
        verify(clientRepository, times(1)).getById(any());
        verify(roleRepository, times(1)).getByName(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testGrantAdministratorRightsWhenUserIsNonExistent() {
        assertThrows(NotFound.class, () -> clientService.grantAdministratorRights(1));
        verify(clientRepository, times(1)).existsById(any());
        verify(clientRepository, never()).getById(any());
        verify(roleRepository, never()).getByName(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testGrantAdministratorRightOk() {
        Role role = new Role();
        Client client = new Client();
        client.setFirstName("SomeName");
        role.setName("ROLE_USER");
        client.setRole(role);
        when(clientRepository.existsById(any(Long.class))).thenReturn(true);
        when(clientRepository.getById(any(Long.class))).thenReturn(client);
        clientService.grantAdministratorRights(1);
        verify(clientRepository, times(2)).existsById(any());
        verify(clientRepository, times(2)).getById(any());
        verify(roleRepository, times(2)).getByName(any());
        verify(clientRepository, times(1)).save(any());
    }

    @Test
    void testRevokeAdministratorRightsWhenUserIsAlreadyUser() {
        Role role = new Role();
        Client client = new Client();
        client.setFirstName("SomeName");
        role.setName("ROLE_ADMIN");
        client.setRole(role);
        when(clientRepository.existsById(any(Long.class))).thenReturn(true);
        when(clientRepository.getById(any(Long.class))).thenReturn(client);
        assertThrows(BadRequest.class, () -> clientService.revokeAdministratorRights(1));
        verify(clientRepository, times(1)).existsById(any());
        verify(clientRepository, times(1)).getById(any());
        verify(roleRepository, times(1)).getByName(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testRevokeAdministratorRightsWhenUserIsNonExistent() {
        assertThrows(NotFound.class, () -> clientService.revokeAdministratorRights(1));
        verify(clientRepository, times(1)).existsById(any());
        verify(clientRepository, never()).getById(any());
        verify(roleRepository, never()).getByName(any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testRevokeAdministratorRightsOk() {
        Role role = new Role();
        Client client = new Client();
        client.setFirstName("SomeName");
        role.setName("ROLE_ADMIN");
        client.setRole(role);
        when(clientRepository.existsById(any(Long.class))).thenReturn(true);
        when(clientRepository.getById(any(Long.class))).thenReturn(client);
        when(roleRepository.getByName(any(String.class))).thenReturn(role);
        clientService.revokeAdministratorRights(1);
        verify(clientRepository, times(2)).existsById(any());
        verify(clientRepository, times(2)).getById(any());
        verify(roleRepository, times(2)).getByName(any());
        verify(clientRepository, times(1)).save(any());
    }

}

