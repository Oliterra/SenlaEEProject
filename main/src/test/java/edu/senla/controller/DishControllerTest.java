package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.dao.DishRepositoryInterface;
import edu.senla.dto.DishDTO;
import edu.senla.entity.Dish;
import edu.senla.enums.DishType;
import edu.senla.service.DishService;
import edu.senla.service.ValidationService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
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
public class DishControllerTest{

    @Autowired
    private DishController dishController;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private DishService dishService;

    @SpyBean
    private ValidationService validationService;

    @SpyBean
    private DishRepositoryInterface dishRepository;

    private Dish dishToOperateWith;

    @SneakyThrows
    @BeforeEach
    void creteDishToOperateWith() {
        dishToOperateWith = new Dish();
        dishToOperateWith.setName("DishToOperateWith");
        dishToOperateWith.setDishType(DishType.SALAD);
        dishRepository.save(dishToOperateWith);
    }

    @AfterEach
    void resetMethodsCallsOnSpies(){
        reset(validationService);
        reset(dishService);
        reset(dishRepository);
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testGetAllDishesOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/dishes"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(dishService, times(1)).getAllDishes();
    }

    @SneakyThrows
    @Test
    void testCreateDishUnauthorizedStatus() {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setName("CorrectName");
        dishDTO.setDishType("meat");
        String dishJson = mapper.writeValueAsString(dishDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        verify(dishService, never()).createDish(any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"USER"})
    @Test
    void testCreateDishForbiddenStatus() {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setName("CorrectName");
        dishDTO.setDishType("meat");
        String dishJson = mapper.writeValueAsString(dishDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(dishService, never()).createDish(any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testCreateDishWhenJsonIsIncorrectBadRequestStatus() {
        String incorrectJson = "incorrectJson";
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(incorrectJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(dishService, never()).createDish(any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testCreateAlreadyExistentDishConflictStatus() {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setName("CorrectName");
        dishDTO.setDishType("meat");
        String dishJson = mapper.writeValueAsString(dishDTO);
        when(dishRepository.getByName(any(String.class))).thenReturn(new Dish());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isConflict());
        verify(dishService, times(1)).createDish(any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testCreateDishWithIncorrectSymbolsInNameBadRequestStatus() {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setName("$%*&)(");
        dishDTO.setDishType("meat");
        String dishJson = mapper.writeValueAsString(dishDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(dishService, times(1)).createDish(any());
        verify(validationService,times(1)).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testCreateDishWithTooShortNameBadRequestStatus() {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setName("d");
        dishDTO.setDishType("meat");
        String dishJson = mapper.writeValueAsString(dishDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(dishService, times(1)).createDish(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testCreateDishWithWrongDishTypeBadRequestStatus() {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setName("CorrectName");
        dishDTO.setDishType("wrong");
        String dishJson = mapper.writeValueAsString(dishDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(dishService, times(1)).createDish(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testCreateDishCreatedStatus() {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setName("CorrectName");
        dishDTO.setDishType("meat");
        String dishJson = mapper.writeValueAsString(dishDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isCreated());
        verify(dishService, times(1)).createDish(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
    }

    @SneakyThrows
    @Test
    void testGetNonExistentDishNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/dishes/33333"))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(dishService, times(1)).getDish(any(Long.class));
    }

    @SneakyThrows
    @Test
    void testGetExistentDishOkStatus() {
        long idOfDishToGet = dishToOperateWith.getId();
        mockMvc.perform(MockMvcRequestBuilders
                .get("/dishes/{idOfDishToGet}", idOfDishToGet))
                .andDo(print())
                .andExpect(status().isOk());
        verify(dishService, times(1)).getDish(any(Long.class));
    }

    @SneakyThrows
    @Test
    void testUpdateDishUnauthorizedStatus() {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setName("UpdatedName");
        dishDTO.setDishType("meat");
        String dishJson = mapper.writeValueAsString(dishDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/dishes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        verify(dishService, never()).updateDish(any(Long.class), any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"USER"})
    @Test
    void testUpdateDishForbiddenStatus() {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setName("UpdatedName");
        dishDTO.setDishType("meat");
        String dishJson = mapper.writeValueAsString(dishDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/dishes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(dishService, never()).updateDish(any(Long.class), any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateDishWhenJsonIsIncorrectBadRequestStatus() {
        String incorrectJson = "incorrectJson";
        mockMvc.perform(MockMvcRequestBuilders
                .put("/dishes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(incorrectJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(dishService, never()).updateDish(any(Long.class), any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateNonExistentDishNotFoundStatus() {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setName("UpdatedName");
        dishDTO.setDishType("meat");
        String dishJson = mapper.writeValueAsString(dishDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/dishes/33333")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(dishService, times(1)).updateDish(any(Long.class), any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateDishWithAlreadyExistentDishNameConflictStatus() {
        long idOfDishToUpdate = dishToOperateWith.getId();
        DishDTO dishDTO = new DishDTO();
        dishDTO.setName("UpdatedName");
        dishDTO.setDishType("meat");
        String dishJson = mapper.writeValueAsString(dishDTO);
        when(dishRepository.getByName(any(String.class))).thenReturn(new Dish());
        mockMvc.perform(MockMvcRequestBuilders
                .put("/dishes/{idOfDishToUpdate}", idOfDishToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isConflict());
        verify(dishService, times(1)).updateDish(any(Long.class), any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateDishWithIncorrectSymbolsInNameBadRequestStatus() {
        long idOfDishToUpdate = dishToOperateWith.getId();
        DishDTO dishDTO = new DishDTO();
        dishDTO.setName("$%*&)(");
        dishDTO.setDishType("meat");
        String dishJson = mapper.writeValueAsString(dishDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/dishes/{idOfDishToUpdate}", idOfDishToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(dishService, times(1)).updateDish(any(Long.class), any());
        verify(validationService,times(1)).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateDishWithTooShortNameBadRequestStatus() {
        long idOfDishToUpdate = dishToOperateWith.getId();
        DishDTO dishDTO = new DishDTO();
        dishDTO.setName("d");
        dishDTO.setDishType("sauce");
        String dishJson = mapper.writeValueAsString(dishDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/dishes/{idOfDishToUpdate}", idOfDishToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(dishService, times(1)).updateDish(any(Long.class), any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateDishWithWrongTypeBadRequestStatus() {
        long idOfDishToUpdate = dishToOperateWith.getId();
        DishDTO dishDTO = new DishDTO();
        dishDTO.setName("UpdatedName");
        dishDTO.setDishType("wrong");
        String dishJson = mapper.writeValueAsString(dishDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/dishes/{idOfDishToUpdate}", idOfDishToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(dishService, times(1)).updateDish(any(Long.class), any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateDishOkStatus() {
        long idOfDishToUpdate = dishToOperateWith.getId();
        DishDTO dishDTO = new DishDTO();
        dishDTO.setName("UpdatedName");
        dishDTO.setDishType("sauce");
        String dishJson = mapper.writeValueAsString(dishDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/dishes/{idOfDishToUpdate}", idOfDishToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(dishJson))
                .andDo(print())
                .andExpect(status().isOk());
        verify(dishService, times(1)).updateDish(any(Long.class), any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
    }

    @SneakyThrows
    @Test
    void testDeleteDishUnauthorizedStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/dishes/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        verify(dishService, never()).deleteDish(any(Long.class));
    }

    @SneakyThrows
    @WithMockUser(roles={"USER"})
    @Test
    void testDeleteDishForbiddenStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/dishes/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(dishService, never()).deleteDish(any(Long.class));
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testDeleteNonExistentDishNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/dishes/33333"))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(dishService, times(1)).deleteDish(any(Long.class));
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testDeleteExistentDishOkStatus() {
        long idOfDishToDelete = dishToOperateWith.getId();
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/dishes/{idOfDishToDelete}", idOfDishToDelete))
                .andDo(print())
                .andExpect(status().isOk());
        verify(dishService, times(1)).deleteDish(any(Long.class));
    }

}
