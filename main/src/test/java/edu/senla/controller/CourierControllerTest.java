package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.config.DatabaseConfig;
import edu.senla.dao.CourierRepository;
import edu.senla.dto.CourierDTO;
import edu.senla.service.CourierService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@ContextConfiguration(classes = {DatabaseConfig.class, CourierController.class, CourierService.class, CourierRepository.class})
class CourierControllerTest {

    @Autowired
    private CourierController courierController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    private CourierDTO courier;
    private CourierDTO updatedCourier;

    private String courierJson;
    private String updatedCourierJson;

    @SneakyThrows
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        courier = new CourierDTO();
        courier.setId(1);
        courier.setFirstName("TestFirstName");
        courier.setLastName("TestLastName");
        courier.setPhone("+37533739373");

        updatedCourier = new CourierDTO();
        updatedCourier.setFirstName("AnotherTestName");
        updatedCourier.setLastName("AnotherTestName");
        updatedCourier.setPhone("+375444444444");

        courierJson = mapper.writeValueAsString(courier);
        updatedCourierJson = mapper.writeValueAsString(updatedCourier);
    }

    @SneakyThrows
    @Test
    void createCourierCreatedStatus() {
        mockMvc.perform(post("/couriers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courierJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    void createCourierConflictStatus() {
        mockMvc.perform(post("/couriers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courierJson))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(post("/couriers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courierJson))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @SneakyThrows
    @Test
    void createCourierBadRequestStatus() {
        mockMvc.perform(post("/couriers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void readCourierOkStatus() {
        mockMvc.perform(post("/couriers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courierJson))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.
                get("/couriers/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void readCourierNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                get("/couriers/{id}", 2))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void updateCourierOkStatus() {
        mockMvc.perform(post("/couriers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courierJson))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.
                put("/couriers/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedCourierJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void updateCourierNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                put("/couriers/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedCourierJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void updateCourierBadRequestStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                put("/couriers/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void deleteCourierOkStatus() {
        mockMvc.perform(post("/couriers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courierJson))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/couriers/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void deleteCourierNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/couriers/{id}", 2))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
