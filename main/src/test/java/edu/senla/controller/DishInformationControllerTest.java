package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.dto.DishDTO;
import edu.senla.dto.DishInformationDTO;
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
/*@ContextConfiguration(classes = {DatabaseConfig.class, DishInformationController.class, DishInformationService.class, DishInformationRepositoryInterface.class,
        DishController.class, DishService.class, DishRepository.class
})*/
public class DishInformationControllerTest {

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
        dish.setDishType("Meat");
        dish.setName("Meat");

        dishInformation = new DishInformationDTO();
        dishInformation.setDishId(1);
        dishInformation.setCaloricContent(333);
        dishInformation.setDescription("400 g");

        updatedDishInformation = new DishInformationDTO();
        updatedDishInformation.setCaloricContent(444);
        updatedDishInformation.setDescription("500 g");

        dishInformationJson = mapper.writeValueAsString(dishInformation);
        updatedDishInformationJson = mapper.writeValueAsString(updatedDishInformation);
        dishJson = mapper.writeValueAsString(dish);
    }

}
