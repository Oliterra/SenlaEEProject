package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.config.DatabaseConfig;
import edu.senla.dao.TypeOfContainerRepository;
import edu.senla.dto.TypeOfContainerDTO;
import edu.senla.service.TypeOfContainerService;
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
@ContextConfiguration(classes = {DatabaseConfig.class, TypeOfContainerController.class, TypeOfContainerService.class, TypeOfContainerRepository.class})
public class TypeOfContainerControllerTest {

    @Autowired
    private TypeOfContainerController typeOfContainerController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    private TypeOfContainerDTO typeOfContainer;
    private TypeOfContainerDTO updatedTypeOfContainer;

    private String typeOfContainerJson;
    private String updatedTypeOfContainerJson;

    @SneakyThrows
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        typeOfContainer = new TypeOfContainerDTO();
        typeOfContainer.setNumberOfCalories(1000);
        typeOfContainer.setName("Medium");
        typeOfContainer.setPrice(25);

        updatedTypeOfContainer = new TypeOfContainerDTO();
        updatedTypeOfContainer.setName("Small");
        updatedTypeOfContainer.setPrice(20);

        typeOfContainerJson = mapper.writeValueAsString(typeOfContainer);
        updatedTypeOfContainerJson = mapper.writeValueAsString(updatedTypeOfContainer);
    }

    @SneakyThrows
    @Test
    public void createTypeOfContainerCreatedStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/typesOfContainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    public void createTypeOfContainerConflictStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/typesOfContainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/typesOfContainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @SneakyThrows
    @Test
    public void createTypeOfContainerBadRequestStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                post("/typesOfContainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    public void readTypeOfContainerOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/typesOfContainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.
                get("/typesOfContainer/{id}", 1000))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void readTypeOfContainerNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                get("/typesOfContainer/{id}", 2000))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    public void updateTypeOfContainerOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/typesOfContainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.
                put("/typesOfContainer/{id}", 1000)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedTypeOfContainerJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void updateTypeOfContainerNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                put("/typesOfContainer/{id}", 2000)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedTypeOfContainerJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    public void updateTypeOfContainerBadRequestStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                put("/typesOfContainer/{id}", 1000)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    public void deleteTypeOfContainerOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/typesOfContainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeOfContainerJson))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/typesOfContainer/{id}", 1000))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void deleteTypeOfContainerNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/typesOfContainer/{id}", 2000))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
