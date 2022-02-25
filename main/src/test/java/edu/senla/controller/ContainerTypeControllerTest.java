package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.impl.ContainerTypeControllerImpl;
import edu.senla.dao.TypeOfContainerRepository;
import edu.senla.model.dto.ContainerTypeDTO;
import edu.senla.model.dto.ContainerTypeForUpdateDTO;
import edu.senla.model.entity.ContainerType;
import edu.senla.service.impl.ContainerTypeServiceImpl;
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
public class ContainerTypeControllerTest {

    @Autowired
    private ContainerTypeControllerImpl typeOfContainerController;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private ContainerTypeServiceImpl typeOfContainerService;

    @SpyBean
    private ValidationServiceImpl validationService;

    @SpyBean
    private TypeOfContainerRepository typeOfContainerRepository;

    private ContainerType containerTypeToOperateWith;

    @SneakyThrows
    @BeforeEach
    void creteTypeOfContainerToOperateWith() {
        containerTypeToOperateWith = new ContainerType();
        containerTypeToOperateWith.setName("XXXXXL");
        //containerTypeToOperateWith.setNumberOfCalories(2300);
        containerTypeToOperateWith.setPrice(45);
        typeOfContainerRepository.save(containerTypeToOperateWith);
    }

    @SneakyThrows
    @Test
    void testCreateTypeOfContainerUnauthorizedStatus() {
        ContainerTypeDTO containerTypeDTO = new ContainerTypeDTO();
        containerTypeDTO.setName("XL");
        containerTypeDTO.setNumberOfCalories(1200);
        containerTypeDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(containerTypeDTO);
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
        ContainerTypeDTO containerTypeDTO = new ContainerTypeDTO();
        containerTypeDTO.setName("XL");
        containerTypeDTO.setNumberOfCalories(1200);
        containerTypeDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(containerTypeDTO);
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
        ContainerTypeDTO containerTypeDTO = new ContainerTypeDTO();
        containerTypeDTO.setName("XXL");
        containerTypeDTO.setNumberOfCalories(1200);
        containerTypeDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(containerTypeDTO);
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
        ContainerTypeDTO containerTypeDTO = new ContainerTypeDTO();
        containerTypeDTO.setName("XXXXXXXL");
        containerTypeDTO.setNumberOfCalories(1250);
        containerTypeDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(containerTypeDTO);
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
        ContainerTypeDTO containerTypeDTO = new ContainerTypeDTO();
        containerTypeDTO.setName("wrong");
        containerTypeDTO.setNumberOfCalories(1200);
        containerTypeDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(containerTypeDTO);
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
        ContainerTypeDTO containerTypeDTO = new ContainerTypeDTO();
        containerTypeDTO.setName("XXXXXXXL");
        containerTypeDTO.setNumberOfCalories(400000000);
        containerTypeDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(containerTypeDTO);
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
        ContainerTypeDTO containerTypeDTO = new ContainerTypeDTO();
        containerTypeDTO.setName("XXXXXXXL");
        containerTypeDTO.setNumberOfCalories(1200);
        containerTypeDTO.setPrice(40);
        String typeOfContainerJson = mapper.writeValueAsString(containerTypeDTO);
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
        ContainerTypeForUpdateDTO typeOfContainerDTO = new ContainerTypeForUpdateDTO();
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
        ContainerTypeForUpdateDTO typeOfContainerDTO = new ContainerTypeForUpdateDTO();
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
        ContainerTypeForUpdateDTO typeOfContainerDTO = new ContainerTypeForUpdateDTO();
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
        //long idOfTypeOfContainerToUpdate = containerTypeToOperateWith.getNumberOfCalories();
        long idOfTypeOfContainerToUpdate = 0;
        ContainerTypeForUpdateDTO typeOfContainerDTO = new ContainerTypeForUpdateDTO();
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
        //long idOfTypeOfContainerToUpdate = containerTypeToOperateWith.getNumberOfCalories();
        long idOfTypeOfContainerToUpdate = 0;
        ContainerTypeForUpdateDTO typeOfContainerDTO = new ContainerTypeForUpdateDTO();
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
        //long idOfTypeOfContainerToUpdate = containerTypeToOperateWith.getNumberOfCalories();
        long idOfTypeOfContainerToUpdate = 0;;
        ContainerTypeForUpdateDTO typeOfContainerDTO = new ContainerTypeForUpdateDTO();
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
