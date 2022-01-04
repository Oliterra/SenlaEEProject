package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.dao.ClientRepositoryInterface;
import edu.senla.dto.ClientFullInfoDTO;
import edu.senla.service.ClientService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = {ClientController.class, ClientService.class, ClientRepositoryInterface.class})
public class ClientControllerTest {

    @Autowired
    private ClientController clientController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;


    private ClientFullInfoDTO client;
    private ClientFullInfoDTO updatedClient;

    private String clientJson;
    private String updatedClientJson;

    private BCryptPasswordEncoder passwordEncoder;

    @SneakyThrows
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        client = new ClientFullInfoDTO();
        client.setFirstName("TestFirstName");
        client.setLastName("TestLastName");
        client.setPhone("+37533739373");
        client.setEmail("test@test.com");
        client.setAddress("Test address");

        updatedClient = new ClientFullInfoDTO();
        updatedClient.setFirstName("AnotherTestName");
        updatedClient.setLastName("AnotherTestName");
        updatedClient.setPhone("+375444444444");
        updatedClient.setEmail("anotherTest@test.com");
        updatedClient.setAddress("Another test address");

        clientJson = mapper.writeValueAsString(client);
        updatedClientJson = mapper.writeValueAsString(updatedClient);

        passwordEncoder = new BCryptPasswordEncoder();
    }

}


