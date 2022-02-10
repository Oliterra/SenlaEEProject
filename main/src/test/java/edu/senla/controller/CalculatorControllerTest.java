package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.impl.CalculatorControllerImpl;
import edu.senla.dao.DishRepository;
import edu.senla.model.dto.ContainerComponentsDTO;
import edu.senla.model.dto.DishInformationDTO;
import edu.senla.model.entity.Dish;
import edu.senla.model.enums.DishType;
import edu.senla.service.impl.ContainerServiceImpl;
import edu.senla.service.DishInformationService;
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
public class CalculatorControllerTest {

    @Autowired
    private CalculatorControllerImpl calculatorController;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private ContainerServiceImpl containerService;

    @SpyBean
    private DishInformationService dishInformationService;

    @SpyBean
    private DishRepository dishRepository;

    private Dish meat;
    private Dish garnish;
    private Dish salad;
    private Dish sauce;

    @SneakyThrows
    @WithMockUser(roles = {"USER"})
    @BeforeEach
    void creteDishesToOperateWith() {
        meat = new Dish();
        meat.setDishType(DishType.MEAT);
        meat.setName("meat");
        Dish savedMeat = dishRepository.save(meat);
        DishInformationDTO meatDishInformationDTO = new DishInformationDTO();
        meatDishInformationDTO.setCaloricContent(300);
        meatDishInformationDTO.setDishId(savedMeat.getId());
        meatDishInformationDTO.setDescription("some description");
        dishInformationService.createDishInformation(meatDishInformationDTO);

        garnish = new Dish();
        garnish.setDishType(DishType.GARNISH);
        garnish.setName("garnish");
        Dish savedGarnish = dishRepository.save(garnish);
        DishInformationDTO garnishDishInformationDTO = new DishInformationDTO();
        garnishDishInformationDTO.setCaloricContent(400);
        garnishDishInformationDTO.setDishId(savedGarnish.getId());
        garnishDishInformationDTO.setDescription("some description");
        dishInformationService.createDishInformation(garnishDishInformationDTO);

        salad = new Dish();
        salad.setDishType(DishType.SALAD);
        salad.setName("salad");
        Dish savedSalad = dishRepository.save(salad);
        DishInformationDTO saladDishInformationDTO = new DishInformationDTO();
        saladDishInformationDTO.setCaloricContent(200);
        saladDishInformationDTO.setDishId(savedSalad.getId());
        saladDishInformationDTO.setDescription("some description");
        dishInformationService.createDishInformation(saladDishInformationDTO);

        sauce = new Dish();
        sauce.setDishType(DishType.SAUCE);
        sauce.setName("sauce");
        Dish savedSauce = dishRepository.save(sauce);
        DishInformationDTO sauceDishInformationDTO = new DishInformationDTO();
        sauceDishInformationDTO.setCaloricContent(200);
        sauceDishInformationDTO.setDishId(savedSauce.getId());
        sauceDishInformationDTO.setDescription("some description");
        dishInformationService.createDishInformation(sauceDishInformationDTO);
    }

    @SneakyThrows
    @Test
    void testGetWeightOfProductsInContainerUnauthorizedStatus() {
        ContainerComponentsDTO containerComponentsDTO = new ContainerComponentsDTO();
        String containerComponentsJson = mapper.writeValueAsString(containerComponentsDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/calculations/containerParams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(containerComponentsJson))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        verify(containerService, never()).calculateWeightOfDishes(any());
    }

    @SneakyThrows
    @WithMockUser(roles = {"USER"})
    @Test
    void testGetWeightOfProductsInContainerWhenJsonIsIncorrectBadRequestStatus() {
        String incorrectJson = "incorrectJson";
        mockMvc.perform(MockMvcRequestBuilders
                .get("/calculations/containerParams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(incorrectJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(containerService, never()).calculateWeightOfDishes(any());
    }

    @SneakyThrows
    @WithMockUser(roles = {"USER"})
    @Test
    void testGetWeightOfProductsInContainerWhenNameIsInvalidBadRequestStatus() {
        Long meatId = meat.getId();
        Long garnishId = garnish.getId();
        Long saladId = salad.getId();
        Long sauceId = sauce.getId();
        ContainerComponentsDTO containerComponentsDTO = new ContainerComponentsDTO();
        containerComponentsDTO.setTypeOfContainer("wrong");
        containerComponentsDTO.setMeat(meatId);
        containerComponentsDTO.setGarnish(garnishId);
        containerComponentsDTO.setSalad(saladId);
        containerComponentsDTO.setSauce(sauceId);
        String containerComponentsJson = mapper.writeValueAsString(containerComponentsDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/calculations/containerParams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(containerComponentsJson))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(containerService, times(1)).calculateWeightOfDishes(any());
    }

    @SneakyThrows
    @WithMockUser(roles = {"USER"})
    @Test
    void testGetWeightOfProductsInContainerWhenComponentsAreInvalidBadRequestStatus() {
        Long meatId = meat.getId();
        Long garnishId = garnish.getId();
        Long saladId = salad.getId();
        Long sauceId = sauce.getId();
        ContainerComponentsDTO containerComponentsDTO = new ContainerComponentsDTO();
        containerComponentsDTO.setTypeOfContainer("wrong");
        containerComponentsDTO.setMeat(meatId);
        containerComponentsDTO.setGarnish(meatId);
        containerComponentsDTO.setSalad(meatId);
        containerComponentsDTO.setSauce(meatId);
        String containerComponentsJson = mapper.writeValueAsString(containerComponentsDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/calculations/containerParams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(containerComponentsJson))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(containerService, times(1)).calculateWeightOfDishes(any());
    }

    @SneakyThrows
    @WithMockUser(roles = {"USER"})
    @Test
    void testGetWeightOfProductsInContainerOkStatus() {
        Long meatId = meat.getId();
        Long garnishId = garnish.getId();
        Long saladId = salad.getId();
        Long sauceId = sauce.getId();
        ContainerComponentsDTO containerComponentsDTO = new ContainerComponentsDTO();
        containerComponentsDTO.setTypeOfContainer("XS");
        containerComponentsDTO.setMeat(meatId);
        containerComponentsDTO.setGarnish(garnishId);
        containerComponentsDTO.setSalad(saladId);
        containerComponentsDTO.setSauce(sauceId);
        String containerComponentsJson = mapper.writeValueAsString(containerComponentsDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/calculations/containerParams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(containerComponentsJson))
                .andDo(print())
                .andExpect(status().isOk());
        verify(containerService, times(1)).calculateWeightOfDishes(any());
    }

}