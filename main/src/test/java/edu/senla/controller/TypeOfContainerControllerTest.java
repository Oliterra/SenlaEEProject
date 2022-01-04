package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.dto.ContainerDTO;
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
//@ContextConfiguration(classes = {DatabaseConfig.class, TypeOfContainerController.class, TypeOfContainerService.class, TypeOfContainerRepository.class})
public class TypeOfContainerControllerTest {

    @Autowired
    private TypeOfContainerController typeOfContainerController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    private ContainerDTO typeOfContainer;
    private ContainerDTO updatedTypeOfContainer;

    private String typeOfContainerJson;
    private String updatedTypeOfContainerJson;

    @SneakyThrows
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        typeOfContainer = new ContainerDTO();
        //typeOfContainer.setName("Medium");

        updatedTypeOfContainer = new ContainerDTO();
        //updatedTypeOfContainer.setName("Small");

        typeOfContainerJson = mapper.writeValueAsString(typeOfContainer);
        updatedTypeOfContainerJson = mapper.writeValueAsString(updatedTypeOfContainer);
    }

}
