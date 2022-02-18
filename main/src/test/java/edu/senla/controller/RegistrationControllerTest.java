package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.impl.RegistrationControllerImpl;
import edu.senla.model.dto.CourierRegistrationRequestDTO;
import edu.senla.model.dto.RegistrationRequestDTO;
import edu.senla.service.impl.ClientServiceImpl;
import edu.senla.service.impl.CourierServiceImpl;
import edu.senla.service.impl.ValidationServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
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
class RegistrationControllerTest{

    @Autowired
    private RegistrationControllerImpl registrationController;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private ClientServiceImpl clientService;

    @SpyBean
    private CourierServiceImpl courierService;

    @SpyBean
    private ValidationServiceImpl validationService;

    @SneakyThrows
    @Test
    void testRegisterClientWhenJsonIsIncorrectBadRequestStatus() {
        String incorrectJson = "incorrectJson";
        mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(incorrectJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, never()).createClient(any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @Test
    void testRegisterClientWithIncorrectSymbolsInNameBadRequestStatus() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("$%*&)(");
        String newRegistrationRequestJson = mapper.writeValueAsString(newRegistrationRequestDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newRegistrationRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, times(1)).createClient(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @Test
    void testRegisterClientWithTooShortNameBadRequestStatus() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("c");
        String newRegistrationRequestJson = mapper.writeValueAsString(newRegistrationRequestDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newRegistrationRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, times(1)).createClient(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @Test
    void testRegisterClientWithInvalidEmailBadRequestStatus() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("CorrectName");
        newRegistrationRequestDTO.setLastName("CorrectName");
        newRegistrationRequestDTO.setEmail("wrong");
        String newRegistrationRequestJson = mapper.writeValueAsString(newRegistrationRequestDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newRegistrationRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, times(1)).createClient(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @Test
    void testRegisterClientWithInvalidPhoneBadRequestStatus() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("CorrectName");
        newRegistrationRequestDTO.setLastName("CorrectName");
        newRegistrationRequestDTO.setEmail("test@test.com");
        newRegistrationRequestDTO.setPhone("wrong");
        String newRegistrationRequestJson = mapper.writeValueAsString(newRegistrationRequestDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newRegistrationRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, times(1)).createClient(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
    }

    @SneakyThrows
    @Test
    void testRegisterClientWithUnconfirmedPasswordBadRequestStatus() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("CorrectName");
        newRegistrationRequestDTO.setLastName("CorrectName");
        newRegistrationRequestDTO.setEmail("test@test.com");
        newRegistrationRequestDTO.setPhone("+375339999999");
        newRegistrationRequestDTO.setPassword("password");
        newRegistrationRequestDTO.setPasswordConfirm("anotherPassword");
        String newRegistrationRequestJson = mapper.writeValueAsString(newRegistrationRequestDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newRegistrationRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, times(1)).createClient(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
    }

    @SneakyThrows
    @Test
    void testRegisterClientCreatedStatus() {
        RegistrationRequestDTO newRegistrationRequestDTO = new RegistrationRequestDTO();
        newRegistrationRequestDTO.setFirstName("CorrectName");
        newRegistrationRequestDTO.setLastName("CorrectName");
        newRegistrationRequestDTO.setEmail("test@test.com");
        newRegistrationRequestDTO.setPhone("+375333333333");
        newRegistrationRequestDTO.setUsername("Username");
        newRegistrationRequestDTO.setPassword("testPassword");
        newRegistrationRequestDTO.setPasswordConfirm("testPassword");
        String newRegistrationRequestJson = mapper.writeValueAsString(newRegistrationRequestDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newRegistrationRequestJson))
                .andDo(print())
                .andExpect(status().isCreated());
        verify(clientService, times(1)).createClient(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isEmailCorrect(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
    }

    @SneakyThrows
    @Test
    void testRegisterCourierWhenJsonIsIncorrectBadRequestStatus() {
        String incorrectJson = "incorrectJson";
        mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/couriers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(incorrectJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(courierService, never()).createCourier(any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @Test
    void testRegisterCourierWithIncorrectSymbolsInNameBadRequestStatus() {
        CourierRegistrationRequestDTO courierRegistrationRequestDTO = new CourierRegistrationRequestDTO();
        courierRegistrationRequestDTO.setFirstName("$%*&)(");
        String courierRegistrationRequestJson = mapper.writeValueAsString(courierRegistrationRequestDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/couriers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courierRegistrationRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(courierService, times(1)).createCourier(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @Test
    void testRegisterCourierWithTooShortNameBadRequestStatus() {
        CourierRegistrationRequestDTO courierRegistrationRequestDTO = new CourierRegistrationRequestDTO();
        courierRegistrationRequestDTO.setFirstName("c");
        String courierRegistrationRequestJson = mapper.writeValueAsString(courierRegistrationRequestDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/couriers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courierRegistrationRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(courierService, times(1)).createCourier(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @Test
    void testRegisterCourierWithInvalidPhoneBadRequestStatus() {
        CourierRegistrationRequestDTO courierRegistrationRequestDTO = new CourierRegistrationRequestDTO();
        courierRegistrationRequestDTO.setFirstName("CorrectName");
        courierRegistrationRequestDTO.setLastName("CorrectName");
        courierRegistrationRequestDTO.setPhone("wrong");
        String courierRegistrationRequestJson = mapper.writeValueAsString(courierRegistrationRequestDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/couriers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courierRegistrationRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(courierService, times(1)).createCourier(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
    }

    @SneakyThrows
    @Test
    void testRegisterCourierWithUnconfirmedPasswordBadRequestStatus() {
        CourierRegistrationRequestDTO courierRegistrationRequestDTO = new CourierRegistrationRequestDTO();
        courierRegistrationRequestDTO.setFirstName("CorrectName");
        courierRegistrationRequestDTO.setLastName("CorrectName");
        courierRegistrationRequestDTO.setPhone("+375339999999");
        courierRegistrationRequestDTO.setPassword("password");
        courierRegistrationRequestDTO.setPasswordConfirm("anotherPassword");
        String courierRegistrationRequestJson = mapper.writeValueAsString(courierRegistrationRequestDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/couriers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courierRegistrationRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(courierService, times(1)).createCourier(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
    }

    @SneakyThrows
    @Test
    void testRegisterCourierCreatedStatus() {
        CourierRegistrationRequestDTO courierRegistrationRequestDTO = new CourierRegistrationRequestDTO();
        courierRegistrationRequestDTO.setFirstName("CorrectName");
        courierRegistrationRequestDTO.setLastName("CorrectName");
        courierRegistrationRequestDTO.setPhone("+375333333333");
        courierRegistrationRequestDTO.setPassword("SomePassword");
        courierRegistrationRequestDTO.setPasswordConfirm("SomePassword");
        String courierRegistrationRequestDTOJson = mapper.writeValueAsString(courierRegistrationRequestDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/couriers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courierRegistrationRequestDTOJson))
                .andDo(print())
                .andExpect(status().isCreated());
        verify(courierService, times(1)).createCourier(any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
    }

}