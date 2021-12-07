package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.config.DatabaseConfig;
import edu.senla.dao.DishInformationRepository;
import edu.senla.dao.DishRepository;
import edu.senla.dto.*;
import edu.senla.service.DishInformationService;
import edu.senla.service.DishService;
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
import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@ContextConfiguration(classes = {DatabaseConfig.class, DishInformationController.class, DishInformationService.class, DishInformationRepository.class,
        DishController.class, DishService.class, DishRepository.class
})
class DishInformationControllerTest {

    @Autowired
    private DishInformationController dishInformationController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    private DishInformationDTO dishInformation;
    private DishInformationDTO updatedDishInformation;
    private DishDTO dish;

    private String dishInformationJson;
    private String updatedDishInformationJson;
    private String dishJson;

    @SneakyThrows
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        dish = new DishDTO();
        dish.setId(1);
        dish.setDishType("Meat");
        dish.setName("Meat");

        dishInformation = new DishInformationDTO();
        dishInformation.setId(1);
        dishInformation.setDishId(1);
        dishInformation.setCaloricContent(333);
        dishInformation.setDescription("400 g");
        dishInformation.setCookingDate(LocalDate.now());
        dishInformation.setExpirationType(LocalDate.now());

        updatedDishInformation = new DishInformationDTO();
        updatedDishInformation.setCaloricContent(444);
        updatedDishInformation.setDescription("500 g");

        dishInformationJson = mapper.writeValueAsString(dishInformation);
        updatedDishInformationJson = mapper.writeValueAsString(updatedDishInformation);
        dishJson = mapper.writeValueAsString(dish);
    }

    @SneakyThrows
    @Test
    void createDishInformationCreatedStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishesInformation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishInformationJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    void createDishInformationNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishesInformation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishInformationJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void createDishInformationBadRequestStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                post("/dishesInformation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void readDishInformationOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishesInformation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishInformationJson))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.
                get("/dishesInformation/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void readDishInformationNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                get("/dishesInformation/{id}", 2))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void updateDishInformationOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishesInformation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishInformationJson))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.
                put("/dishesInformation/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedDishInformationJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void updateDishInformationNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                put("/dishesInformation/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedDishInformationJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void updateDishInformationBadRequestStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                put("/dishesInformation/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void deleteDishInformationOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishesInformation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishInformationJson))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/dishesInformation/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void deleteDishInformationNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/dishesInformation/{id}", 2))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}