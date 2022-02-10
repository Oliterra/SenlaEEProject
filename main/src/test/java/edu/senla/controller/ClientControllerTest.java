package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.impl.ClientControllerImpl;
import edu.senla.dao.ClientRepository;
import edu.senla.model.dto.ClientMainInfoDTO;
import edu.senla.model.entity.Client;
import edu.senla.service.impl.ClientServiceImpl;
import edu.senla.service.impl.ValidationServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ClientControllerTest {

    @Autowired
    private ClientControllerImpl registrationController;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private ClientServiceImpl clientService;

    @SpyBean
    private ValidationServiceImpl validationService;

    @SpyBean
    private ClientRepository clientRepository;

    private Client clientToOperateWith;

    @SneakyThrows
    @BeforeEach
    void creteClientToOperateWith() {
        clientToOperateWith = new Client();
        clientToOperateWith.setFirstName("CorrectName");
        clientToOperateWith.setLastName("CorrectName");
        clientToOperateWith.setEmail("test@test.com");
        clientToOperateWith.setPhone("+375333333333");
        clientToOperateWith.setUsername("Username");
        clientToOperateWith.setPassword("testPassword");
        clientRepository.save(clientToOperateWith);
    }

    @SneakyThrows
    @Test
    void testGetAllClientsUnauthorizedStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/clients"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        verify(clientService, never()).getAllClients(10);
    }

    @SneakyThrows
    @WithMockUser(roles={"USER"})
    @Test
    void testGetAllClientsForbiddenStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/clients"))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(clientService, never()).getAllClients(10);
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testGetAllClientsOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/clients"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(clientService, times(1)).getAllClients(10);
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testGetNonExistentClientNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/clients/33333"))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(clientService, times(1)).getClient(any(Long.class));
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testGetExistentClientOkStatus() {
        long idOfClientToGet = clientToOperateWith.getId();
        mockMvc.perform(MockMvcRequestBuilders
                .get("/clients/{idOfClientToGet}", idOfClientToGet))
                .andDo(print())
                .andExpect(status().isOk());
        verify(clientService, times(1)).getClient(any(Long.class));
    }

    @SneakyThrows
    @Test
    void testUpdateClientUnauthorizedStatus() {
        ClientMainInfoDTO clientMainInfoDTO = new ClientMainInfoDTO();
        clientMainInfoDTO.setFirstName("CorrectName");
        String clientMainInfoJson = mapper.writeValueAsString(clientMainInfoDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientMainInfoJson))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        verify(clientService, never()).updateClient(any(Long.class), any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"COURIER"})
    @Test
    void testUpdateClientForbiddenStatus() {
        ClientMainInfoDTO clientMainInfoDTO = new ClientMainInfoDTO();
        clientMainInfoDTO.setFirstName("CorrectName");
        String clientMainInfoJson = mapper.writeValueAsString(clientMainInfoDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientMainInfoJson))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(clientService, never()).updateClient(any(Long.class), any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateClientWhenJsonIsIncorrectBadRequestStatus() {
        String incorrectJson = "incorrectJson";
        mockMvc.perform(MockMvcRequestBuilders
                .put("/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(incorrectJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, never()).updateClient(any(Long.class), any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateClientWithIncorrectSymbolsInNameBadRequestStatus() {
        long idOfClientToUpdate = clientToOperateWith.getId();
        ClientMainInfoDTO clientMainInfoDTO = new ClientMainInfoDTO();
        clientMainInfoDTO.setFirstName("$%*&)(");
        String clientMainInfoJson = mapper.writeValueAsString(clientMainInfoDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/clients/{idOfClientToUpdate}", idOfClientToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientMainInfoJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, times(1)).updateClient(any(Long.class), any());
        verify(validationService,  times(1)).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateClientWithTooShortNameBadRequestStatus() {
        long idOfClientToUpdate = clientToOperateWith.getId();
        ClientMainInfoDTO clientMainInfoDTO = new ClientMainInfoDTO();
        clientMainInfoDTO.setFirstName("c");
        String clientMainInfoJson = mapper.writeValueAsString(clientMainInfoDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/clients/{idOfClientToUpdate}", idOfClientToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientMainInfoJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, times(1)).updateClient(any(Long.class), any());
        verify(validationService,  times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateClientWithInvalidEmailBadRequestStatus() {
        long idOfClientToUpdate = clientToOperateWith.getId();
        ClientMainInfoDTO clientMainInfoDTO = new ClientMainInfoDTO();
        clientMainInfoDTO.setFirstName("CorrectName");
        clientMainInfoDTO.setLastName("CorrectName");
        clientMainInfoDTO.setEmail("wrong");
        String clientMainInfoJson = mapper.writeValueAsString(clientMainInfoDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/clients/{idOfClientToUpdate}", idOfClientToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientMainInfoJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, times(1)).updateClient(any(Long.class), any());
        verify(validationService,  times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateClientWithInvalidPhoneBadRequestStatus() {
        long idOfClientToUpdate = clientToOperateWith.getId();
        ClientMainInfoDTO clientMainInfoDTO = new ClientMainInfoDTO();
        clientMainInfoDTO.setFirstName("CorrectName");
        clientMainInfoDTO.setLastName("CorrectName");
        clientMainInfoDTO.setEmail("test@test.com");
        clientMainInfoDTO.setPhone("wrong");
        String clientMainInfoJson = mapper.writeValueAsString(clientMainInfoDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/clients/{idOfClientToUpdate}", idOfClientToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientMainInfoJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, times(1)).updateClient(any(Long.class), any());
        verify(validationService,  times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService,times(1)).isPhoneCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateClientOkStatus() {
        long idOfClientToUpdate = clientToOperateWith.getId();
        ClientMainInfoDTO clientMainInfoDTO = new ClientMainInfoDTO();
        clientMainInfoDTO.setFirstName("UpdatedName");
        clientMainInfoDTO.setLastName("UpdatedName");
        clientMainInfoDTO.setEmail("updated@test.com");
        clientMainInfoDTO.setPhone("+3756666666");
        String clientMainInfoJson = mapper.writeValueAsString(clientMainInfoDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/clients/{idOfClientToUpdate}", idOfClientToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientMainInfoJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, times(1)).updateClient(any(Long.class), any());
        verify(validationService,times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testDeleteNonExistentClientNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/clients/33333"))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(clientService, times(1)).deleteClient(any(Long.class));
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testDeleteExistentClientOkStatus() {
        long idOfClientToDelete = clientToOperateWith.getId();
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/clients/{idOfClientToDelete}", idOfClientToDelete))
                .andDo(print())
                .andExpect(status().isOk());
        verify(clientService, times(1)).deleteClient(any(Long.class));
    }

}


