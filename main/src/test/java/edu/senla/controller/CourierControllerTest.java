package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.dto.CourierFullInfoDTO;
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
//@ContextConfiguration(classes = {DatabaseConfig.class, SecurityConfig.class, CourierController.class, CourierService.class, ClientService.class, CourierRepository.class})
public class CourierControllerTest {

    @Autowired
    private CourierController courierController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    private CourierFullInfoDTO courier;
    private CourierFullInfoDTO updatedCourier;

    private String courierJson;
    private String updatedCourierJson;

    @SneakyThrows
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        courier = new CourierFullInfoDTO();
        courier.setFirstName("TestFirstName");
        courier.setLastName("TestLastName");
        courier.setPhone("+37533739373");

        updatedCourier = new CourierFullInfoDTO();
        updatedCourier.setFirstName("AnotherTestName");
        updatedCourier.setLastName("AnotherTestName");
        updatedCourier.setPhone("+375444444444");

        courierJson = mapper.writeValueAsString(courier);
        updatedCourierJson = mapper.writeValueAsString(updatedCourier);
    }

}
