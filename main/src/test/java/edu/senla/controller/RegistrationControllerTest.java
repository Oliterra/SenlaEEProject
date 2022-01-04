package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.dto.RegistrationRequestDTO;
import edu.senla.service.ClientService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = {RegistrationController.class, ClientService.class})
class RegistrationControllerTest{

    @Autowired
    private RegistrationController registrationController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    private RegistrationRequestDTO registrationRequestDTO;

    private String clientRegistrationInfoJson;
    private String registrationRequestJson;

    /*@SneakyThrows
    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        clientRegistrationInfoDTO = new ClientRegistrationInfoDTO();
        clientRegistrationInfoDTO.setFirstName("TestFirstName");
        clientRegistrationInfoDTO.setLastName("TestLastName");
        clientRegistrationInfoDTO.setEmail("test@test.com");

        registrationRequestDTO = new RegistrationRequestDTO();
        registrationRequestDTO.setPassword("testPassword");
        registrationRequestDTO.setPasswordConfirm("testPassword");
        registrationRequestDTO.setUsername("TestUsername");

        clientRegistrationInfoJson = mapper.writeValueAsString(clientRegistrationInfoDTO);
        registrationRequestJson = mapper.writeValueAsString(registrationRequestDTO);
    }

    @SneakyThrows
    @Test
    void registerClientCreatedStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientRegistrationInfoJson + registrationRequestJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }*/

    /*@SneakyThrows
    @Test
    void createClientConflictStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @SneakyThrows
    @Test
    void createClientBadRequestStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }*/

    /*@Test
    void registerClient() {
    }*/

}