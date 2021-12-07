package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.config.DatabaseConfig;
import edu.senla.dao.ClientRepository;
import edu.senla.dto.ClientDTO;
import edu.senla.service.ClientService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@ContextConfiguration(classes = {DatabaseConfig.class, ClientController.class, ClientService.class, ClientRepository.class})
class ClientControllerTest {

    @Autowired
    private ClientController clientController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    private ClientDTO client;
    private ClientDTO updatedClient;

    private String clientJson;
    private String updatedClientJson;

    @SneakyThrows
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        client = new ClientDTO();
        client.setId(1);
        client.setFirstName("TestFirstName");
        client.setLastName("TestLastName");
        client.setPhone("+37533739373");
        client.setEmail("test@test.com");
        client.setAddress("Test address");

        updatedClient = new ClientDTO();
        updatedClient.setFirstName("AnotherTestName");
        updatedClient.setLastName("AnotherTestName");
        updatedClient.setPhone("+375444444444");
        updatedClient.setEmail("anotherTest@test.com");
        updatedClient.setAddress("Another test address");

        clientJson = mapper.writeValueAsString(client);
        updatedClientJson = mapper.writeValueAsString(updatedClient);
    }

    @SneakyThrows
    @Test
    void createClientCreatedStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @SneakyThrows
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
    }

    @SneakyThrows
    @Test
    void readClientOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.
                get("/clients/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void readClientNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                get("/clients/{id}", 2))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void updateClientOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.
                put("/clients/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedClientJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void updateClientNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                put("/clients/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedClientJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void updateClientBadRequestStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                put("/clients/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void deleteClientOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/clients/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void deleteClientNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/clients/{id}", 2))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}

