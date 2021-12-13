package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.config.DatabaseConfig;
import edu.senla.dao.DishRepository;
import edu.senla.dto.DishDTO;
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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@ContextConfiguration(classes = {DatabaseConfig.class, DishController.class, DishService.class, DishRepository.class})
class DishControllerTest {

    @Autowired
    private DishController dishController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    private DishDTO dish;
    private DishDTO updatedDish;

    private String dishJson;
    private String updatedDishJson;

    @SneakyThrows
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        dish = new DishDTO();
        dish.setId(1);
        dish.setDishType("Porridge");
        dish.setName("Oatmeal porrige");

        updatedDish = new DishDTO();
        dish.setName("Semolina porrige");

        dishJson = mapper.writeValueAsString(dish);
        updatedDishJson = mapper.writeValueAsString(updatedDish);
    }

    @SneakyThrows
    @Test
    void createDishCreatedStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    void createDishConflictStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @SneakyThrows
    @Test
    void createDishBadRequestStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void readDishOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.
                get("/dishes/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void readDishNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                get("/dishes/{id}", 2))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void updateDishOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.
                put("/dishes/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedDishJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void updateDishNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                put("/dishes/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedDishJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void updateDishBadRequestStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                put("/dishes/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void deleteDishOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/dishes/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void deleteDishNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/dishes/{id}", 2))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
