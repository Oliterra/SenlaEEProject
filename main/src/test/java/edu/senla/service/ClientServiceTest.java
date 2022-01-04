package edu.senla.service;

import edu.senla.dao.ClientRepositoryInterface;
import edu.senla.dao.RoleRepositoryInterface;
import edu.senla.dto.ClientFullInfoDTO;
import edu.senla.entity.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepositoryInterface clientRepository;
    @Mock
    private RoleRepositoryInterface roleRepository;

    @Spy
    private ModelMapper mapper;
    @Spy
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientService clientService;

    private Client client;
    private ClientFullInfoDTO clientFullInfoDTO;

    private int clientId;
    private String clientFirstName;
    private String clientLastName;
    private String clientEmail;

    private String clientPassword;
    private String clientPasswordConfirmation;

    @BeforeEach
    void setup() {
        clientId = 1;
        clientFirstName = "TestFirstName";
        clientLastName = "TestLastName";
        clientEmail = "testEmail@test.com";

        client = new Client();
        clientFullInfoDTO = new ClientFullInfoDTO();

        client.setId(clientId);
        client.setFirstName(clientFirstName);
        client.setLastName(clientLastName);
        client.setEmail(clientEmail);
    }

    /*@Test
    void createClient() {
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        clientService.createClient(clientFullInfoDTO);
        verify(clientRepository, times(1)).save(any(Client.class));
    }*/

    /*@Test
    void isExistentClientExists() {
        when(clientRepository.getByEmail(any(String.class))).thenReturn(client);
        boolean isClientExists = clientService.isClientExists(clientEmail);
        verify(clientRepository, times(1)).getByEmail(any(String.class));
        assertTrue(isClientExists);
    }

    @Test
    void isNonExistentClientExists() {
        when(clientRepository.getByEmail(any(String.class))).thenReturn(null);
        boolean isClientExists = clientService.isClientExists(clientEmail);
        verify(clientRepository, times(1)).getByEmail(any(String.class));
        assertFalse(isClientExists);
    }*/

   /* @Test
    void isEqualPasswordsConfirmed() {
        RegistrationRequestDTO registrationRequestDTO = new RegistrationRequestDTO();
        registrationRequestDTO.setPassword("testPassword");
        registrationRequestDTO.setPasswordConfirm("testPassword");
        boolean isPasswordConfirmed = clientService.isPasswordConfirmed(registrationRequestDTO);
        assertTrue(isPasswordConfirmed);
    }

    @Test
    void isUnequalPasswordsConfirmed() {
        RegistrationRequestDTO registrationRequestDTO = new RegistrationRequestDTO();
        registrationRequestDTO.setPassword("testPassword");
        registrationRequestDTO.setPasswordConfirm("anotherTestPassword");
        boolean isPasswordConfirmed = clientService.isPasswordConfirmed(registrationRequestDTO);
        assertFalse(isPasswordConfirmed);
    }*/

    /*@Test
    void generateFullClientInformation() {
        ClientRegistrationInfoDTO clientRegistrationInfoDTO = new ClientRegistrationInfoDTO();
        clientRegistrationInfoDTO.setFirstName("TestName");
        RegistrationRequestDTO registrationRequestDTO = new RegistrationRequestDTO();
        registrationRequestDTO.setUsername("TestUsername");
        registrationRequestDTO.setPassword("testPassword");
        Role role = new Role();
        role.setName("ROLE_USER");
        when(roleRepository.getByName(any(String.class))).thenReturn(role);
        clientFullInfoDTO = clientService.generateFullClientInformation(clientRegistrationInfoDTO, registrationRequestDTO);
        verify(roleRepository, times(1)).getByName(any(String.class));
        assertEquals(clientRegistrationInfoDTO.getFirstName(), clientFullInfoDTO.getFirstName());
        assertEquals(registrationRequestDTO.getUsername(), registrationRequestDTO.getUsername());
        assertEquals(role, clientFullInfoDTO.getRole());
        assertNotEquals(registrationRequestDTO.getPassword(), clientFullInfoDTO.getPassword());
    }*/

}

