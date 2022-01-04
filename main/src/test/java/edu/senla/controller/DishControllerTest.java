package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.dto.DishDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
//@ContextConfiguration(classes = {DatabaseConfig.class, DishController.class, DishService.class, DishRepository.class})
public class DishControllerTest {

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
        dish.setDishType("Porridge");
        dish.setName("Oatmeal porrige");

        updatedDish = new DishDTO();
        dish.setName("Semolina porrige");

        dishJson = mapper.writeValueAsString(dish);
        updatedDishJson = mapper.writeValueAsString(updatedDish);
    }

}
