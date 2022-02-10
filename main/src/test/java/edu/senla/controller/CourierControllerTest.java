package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.impl.CourierControllerImpl;
import edu.senla.dao.CourierRepository;
import edu.senla.model.dto.CourierMainInfoDTO;
import edu.senla.model.entity.Courier;
import edu.senla.service.impl.CourierServiceImpl;
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
public class CourierControllerTest {

    @Autowired
    private CourierControllerImpl registrationController;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private CourierServiceImpl courierService;

    @SpyBean
    private ValidationServiceImpl validationService;

    @SpyBean
    private CourierRepository courierRepository;

    private Courier courierToOperateWith;

    @SneakyThrows
    @BeforeEach
    void creteDishToOperateWith() {
        courierToOperateWith = new Courier();
        courierToOperateWith.setFirstName("CorrectName");
        courierToOperateWith.setLastName("CorrectName");
        courierToOperateWith.setPhone("+375333333333");
        courierToOperateWith.setPassword("testPassword");
        courierRepository.save(courierToOperateWith);
    }

    @SneakyThrows
    @Test
    void testGetAllCouriersUnauthorizedStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/couriers"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        verify(courierService, never()).getAllCouriers(10);
    }

    @SneakyThrows
    @WithMockUser(roles={"USER"})
    @Test
    void testGetAllCouriersForbiddenStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/couriers"))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(courierService, never()).getAllCouriers(10);
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testGetAllCouriersOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/couriers"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(courierService, times(1)).getAllCouriers(10);
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testGetNonExistentCourierNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/clients/33333"))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(courierService, never()).getCourier(any(Long.class));
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testGetExistentCourierOkStatus() {
        long idOfCourierToGet = courierToOperateWith.getId();
        mockMvc.perform(MockMvcRequestBuilders
                .get("/couriers/{idOfCourierToGet}", idOfCourierToGet))
                .andDo(print())
                .andExpect(status().isOk());
        verify(courierService, times(1)).getCourier(any(Long.class));
    }

    @SneakyThrows
    @Test
    void testUpdateCourierUnauthorizedStatus() {
        CourierMainInfoDTO courierMainInfoDTO = new CourierMainInfoDTO();
        courierMainInfoDTO.setFirstName("CorrectName");
        String courierMainInfoJson = mapper.writeValueAsString(courierMainInfoDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/couriers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courierMainInfoJson))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        verify(courierService, never()).updateCourier(any(Long.class), any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"USER"})
    @Test
    void testUpdateCourierForbiddenStatus() {
        CourierMainInfoDTO courierMainInfoDTO = new CourierMainInfoDTO();
        courierMainInfoDTO.setFirstName("CorrectName");
        String courierMainInfoJson = mapper.writeValueAsString(courierMainInfoDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/couriers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courierMainInfoJson))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(courierService, never()).updateCourier(any(Long.class), any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateCourierWhenJsonIsIncorrectBadRequestStatus() {
        String incorrectJson = "incorrectJson";
        mockMvc.perform(MockMvcRequestBuilders
                .put("/couriers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(incorrectJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(courierService, never()).updateCourier(any(Long.class), any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateCourierWithIncorrectSymbolsInNameBadRequestStatus() {
        long idOfCourierToUpdate = courierToOperateWith.getId();
        CourierMainInfoDTO courierMainInfoDTO = new CourierMainInfoDTO();
        courierMainInfoDTO.setFirstName("$%*&)(");
        String courierMainInfoJson = mapper.writeValueAsString(courierMainInfoDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/couriers/{idOfCourierToUpdate}", idOfCourierToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(courierMainInfoJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(courierService, times(1)).updateCourier(any(Long.class), any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(validationService, never()).isEmailCorrect(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateCourierWithTooShortNameBadRequestStatus() {
        long idOfCourierToUpdate = courierToOperateWith.getId();
        CourierMainInfoDTO courierMainInfoDTO = new CourierMainInfoDTO();
        courierMainInfoDTO.setFirstName("c");
        String courierMainInfoJson = mapper.writeValueAsString(courierMainInfoDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/couriers/{idOfCourierToUpdate}", idOfCourierToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(courierMainInfoJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(courierService, times(1)).updateCourier(any(Long.class), any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
        verify(validationService, never()).isPhoneCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testRUpdateCourierWithInvalidPhoneBadRequestStatus() {
        long idOfCourierToUpdate = courierToOperateWith.getId();
        CourierMainInfoDTO courierMainInfoDTO = new CourierMainInfoDTO();
        courierMainInfoDTO.setFirstName("CorrectName");
        courierMainInfoDTO.setLastName("CorrectName");
        courierMainInfoDTO.setPhone("wrong");
        String courierMainInfoJson = mapper.writeValueAsString(courierMainInfoDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/couriers/{idOfCourierToUpdate}", idOfCourierToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(courierMainInfoJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(courierService, times(1)).updateCourier(any(Long.class), any());
        verify(validationService, times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateCourierOkStatus() {
        long idOfCourierToUpdate = courierToOperateWith.getId();
        CourierMainInfoDTO courierMainInfoDTO = new CourierMainInfoDTO();
        courierMainInfoDTO.setFirstName("UpdatedName");
        courierMainInfoDTO.setLastName("UpdatedName");
        courierMainInfoDTO.setPhone("+375336666666");
        String courierMainInfoJson = mapper.writeValueAsString(courierMainInfoDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/couriers/{idOfCourierToUpdate}", idOfCourierToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(courierMainInfoJson))
                .andDo(print())
                .andExpect(status().isOk());
        verify(courierService, times(1)).updateCourier(any(Long.class), any());
        verify(validationService,times(2)).isNameCorrect(any());
        verify(validationService, times(2)).isNameLengthValid(any());
        verify(validationService, times(1)).isPhoneCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testDeleteNonExistentCourierNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/couriers/33333"))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(courierService, times(1)).deleteCourier(any(Long.class));
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testDeleteExistentCourierOkStatus() {
        long idOfClientToDelete = courierToOperateWith.getId();
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/couriers/{idOfClientToDelete}", idOfClientToDelete))
                .andDo(print())
                .andExpect(status().isOk());
        verify(courierService, times(1)).deleteCourier(any(Long.class));
    }

}
