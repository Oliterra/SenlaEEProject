package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.dao.TypeOfContainerRepositoryInterface;
import edu.senla.dto.TypeOfContainerDTO;
import edu.senla.dto.TypeOfContainerForUpdateDTO;
import edu.senla.entity.TypeOfContainer;
import edu.senla.service.TypeOfContainerService;
import edu.senla.service.ValidationService;
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
public class TypeOfContainerControllerTest {

    @Autowired
    private TypeOfContainerController typeOfContainerController;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private TypeOfContainerService typeOfContainerService;

    @SpyBean
    private ValidationService validationService;

    @SpyBean
    private TypeOfContainerRepositoryInterface typeOfContainerRepository;

    private TypeOfContainer typeOfContainerToOperateWith;

    @SneakyThrows
    @BeforeEach
    void creteTypeOfContainerToOperateWith() {
        typeOfContainerToOperateWith = new TypeOfContainer();
        typeOfContainerToOperateWith.setName("XXXXXL");
        typeOfContainerToOperateWith.setNumberOfCalories(2300);
        typeOfContainerToOperateWith.setPrice(45);
        typeOfContainerRepository.save(typeOfContainerToOperateWith);
    }

    @SneakyThrows
    @Test
    void testCreateTypeOfContainerUnauthorizedStatus() {
        TypeOfContainerDTO typeOfContainerDTO = new TypeOfContainerDTO();
        typeOfContainerDTO.setName("XL");
        typeOfContainerDTO.setNumberOfCalories(1200);
        typeOfContainerDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(typeOfContainerDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/typesOfContainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        verify(typeOfContainerService, never()).createTypeOfContainer(any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"USER"})
    @Test
    void testCreateTypeOfContainerForbiddenStatus() {
        TypeOfContainerDTO typeOfContainerDTO = new TypeOfContainerDTO();
        typeOfContainerDTO.setName("XL");
        typeOfContainerDTO.setNumberOfCalories(1200);
        typeOfContainerDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(typeOfContainerDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/typesOfContainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(typeOfContainerService, never()).createTypeOfContainer(any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testCreateTypeOfContainerWhenJsonIsIncorrectBadRequestStatus() {
        String incorrectJson = "incorrectJson";
        mockMvc.perform(MockMvcRequestBuilders
                .post("/typesOfContainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(incorrectJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(typeOfContainerService, never()).createTypeOfContainer(any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testCreateTypeOfContainerWithAlreadyExistentNameConflictStatus(){
        TypeOfContainerDTO typeOfContainerDTO = new TypeOfContainerDTO();
        typeOfContainerDTO.setName("XXL");
        typeOfContainerDTO.setNumberOfCalories(1200);
        typeOfContainerDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(typeOfContainerDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/typesOfContainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isConflict());
        verify(typeOfContainerService, times(1)).createTypeOfContainer(any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testCreateTypeOfContainerWithAlreadyExistentNumberOfCaloriesConflictStatus(){
        TypeOfContainerDTO typeOfContainerDTO = new TypeOfContainerDTO();
        typeOfContainerDTO.setName("XXXXXXXL");
        typeOfContainerDTO.setNumberOfCalories(1250);
        typeOfContainerDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(typeOfContainerDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/typesOfContainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isConflict());
        verify(typeOfContainerService, times(1)).createTypeOfContainer(any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testCreateTypeOfContainerWithInvalidNameBadRequestStatus(){
        TypeOfContainerDTO typeOfContainerDTO = new TypeOfContainerDTO();
        typeOfContainerDTO.setName("wrong");
        typeOfContainerDTO.setNumberOfCalories(1200);
        typeOfContainerDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(typeOfContainerDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/typesOfContainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(typeOfContainerService, times(1)).createTypeOfContainer(any());
        verify(validationService, times(1)).isTypeOContainerNameCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testCreateTypeOfContainerWithInvalidNumberOfCaloriesBadRequestStatus(){
        TypeOfContainerDTO typeOfContainerDTO = new TypeOfContainerDTO();
        typeOfContainerDTO.setName("XXXXXXXL");
        typeOfContainerDTO.setNumberOfCalories(400000000);
        typeOfContainerDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(typeOfContainerDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/typesOfContainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(typeOfContainerService, times(1)).createTypeOfContainer(any());
        verify(validationService, times(1)).isTypeOContainerNameCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testCreateTypeOfContainerCreatedStatus() {
        TypeOfContainerDTO typeOfContainerDTO = new TypeOfContainerDTO();
        typeOfContainerDTO.setName("XXXXXXXL");
        typeOfContainerDTO.setNumberOfCalories(1200);
        typeOfContainerDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(typeOfContainerDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/typesOfContainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isCreated());
        verify(typeOfContainerService, times(1)).createTypeOfContainer(any());
        verify(validationService,times(1)).isTypeOContainerNameCorrect(any());
    }

    @SneakyThrows
    @Test
    void testUpdateTypeOfContainerUnauthorizedStatus() {
        TypeOfContainerForUpdateDTO typeOfContainerDTO = new TypeOfContainerForUpdateDTO();
        typeOfContainerDTO.setName("XL");
        typeOfContainerDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(typeOfContainerDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/typesOfContainer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        verify(typeOfContainerService, never()).updateTypeOfContainer(any(Long.class), any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"USER"})
    @Test
    void testUpdateTypeOfContainerForbiddenStatus() {
        TypeOfContainerForUpdateDTO typeOfContainerDTO = new TypeOfContainerForUpdateDTO();
        typeOfContainerDTO.setName("XL");
        typeOfContainerDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(typeOfContainerDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/typesOfContainer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(typeOfContainerService, never()).updateTypeOfContainer(any(Long.class), any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateTypeOfContainerWhenJsonIsIncorrectBadRequestStatus() {
        String incorrectJson = "incorrectJson";
        mockMvc.perform(MockMvcRequestBuilders
                .put("/typesOfContainer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(incorrectJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(typeOfContainerService, never()).updateTypeOfContainer(any(Long.class), any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateNonExistentTypeOfContainerNotFoundStatus() {
        TypeOfContainerForUpdateDTO typeOfContainerDTO = new TypeOfContainerForUpdateDTO();
        typeOfContainerDTO.setName("XXXXXXXL");
        typeOfContainerDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(typeOfContainerDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/typesOfContainer/7777777")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(typeOfContainerService, times(1)).updateTypeOfContainer(any(Long.class), any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateTypeOfContainerWithAlreadyExistentNameConflictStatus() {
        long idOfTypeOfContainerToUpdate = typeOfContainerToOperateWith.getNumberOfCalories();
        TypeOfContainerForUpdateDTO typeOfContainerDTO = new TypeOfContainerForUpdateDTO();
        typeOfContainerDTO.setName("XXL");
        typeOfContainerDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(typeOfContainerDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/typesOfContainer/{idOfTypeOfContainerToUpdate}", idOfTypeOfContainerToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isConflict());
        verify(typeOfContainerService, times(1)).updateTypeOfContainer(any(Long.class), any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateTypeOfContainerWithInvalidNameBadRequestStatus() {
        long idOfTypeOfContainerToUpdate = typeOfContainerToOperateWith.getNumberOfCalories();
        TypeOfContainerForUpdateDTO typeOfContainerDTO = new TypeOfContainerForUpdateDTO();
        typeOfContainerDTO.setName("wrong");
        typeOfContainerDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(typeOfContainerDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/typesOfContainer/{idOfTypeOfContainerToUpdate}", idOfTypeOfContainerToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(typeOfContainerService, times(1)).updateTypeOfContainer(any(Long.class), any());
        verify(validationService, times(1)).isTypeOContainerNameCorrect(any());
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testUpdateTypeOfContainerOkStatus() {
        long idOfTypeOfContainerToUpdate = typeOfContainerToOperateWith.getNumberOfCalories();
        TypeOfContainerForUpdateDTO typeOfContainerDTO = new TypeOfContainerForUpdateDTO();
        typeOfContainerDTO.setName("XXXXXXXL");
        typeOfContainerDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(typeOfContainerDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/typesOfContainer/{idOfTypeOfContainerToUpdate}", idOfTypeOfContainerToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isOk());
        verify(typeOfContainerService, times(1)).updateTypeOfContainer(any(Long.class), any());
        verify(validationService,times(1)).isTypeOContainerNameCorrect(any());
    }

}
