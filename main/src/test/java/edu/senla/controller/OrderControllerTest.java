package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.dto.ClientFullInfoDTO;
import edu.senla.dto.OrderDTO;
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
/*@ContextConfiguration(classes = {DatabaseConfig.class, OrderController.class, OrderService.class, OrderRepository.class,
        ClientController.class, ClientService.class, ClientRepository.class,
        CourierController.class, CourierService.class, CourierRepository.class
})*/
public class OrderControllerTest {

    @Autowired
    private OrderController orderController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    private OrderDTO order;
    private OrderDTO updatedOrder;
    private ClientFullInfoDTO client;

    private String orderJson;
    private String updatedOrderJson;
    private String clientJson;

    @SneakyThrows
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        client = new ClientFullInfoDTO();
        client.setFirstName("TestFirstName");
        client.setLastName("TestLastName");
        client.setPhone("+37533739373");
        client.setEmail("test@test.com");
        client.setAddress("Test address");

        order = new OrderDTO();
        order.setClientId(1);
        order.setStatus("new");
        order.setPaymentType("by card");

        updatedOrder = new OrderDTO();
        order.setStatus("in process");
        order.setPaymentType("by cash");

        orderJson = mapper.writeValueAsString(order);
        updatedOrderJson = mapper.writeValueAsString(updatedOrder);
        clientJson = mapper.writeValueAsString(client);
    }

}
