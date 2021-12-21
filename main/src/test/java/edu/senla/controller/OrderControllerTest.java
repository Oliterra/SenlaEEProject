package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.dto.ClientDTO;
import edu.senla.dto.OrderDTO;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private ClientDTO client;

    private String orderJson;
    private String updatedOrderJson;
    private String clientJson;

    @SneakyThrows
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        client = new ClientDTO();
        client.setId(1);
        client.setFirstName("TestFirstName");
        client.setLastName("TestLastName");
        client.setPhone("+37533739373");
        client.setEmail("test@test.com");
        client.setAddress("Test address");

        order = new OrderDTO();
        order.setId(1);
        order.setClientId(1);
        order.setStatus("new");
        order.setPaymentType("by card");
        order.setDate(LocalDate.now());

        updatedOrder = new OrderDTO();
        order.setStatus("in process");
        order.setPaymentType("by cash");

        orderJson = mapper.writeValueAsString(order);
        updatedOrderJson = mapper.writeValueAsString(updatedOrder);
        clientJson = mapper.writeValueAsString(client);
    }

    @SneakyThrows
    @Test
    public void createOrderCreatedStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    public void createOrderNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    public void createOrderBadRequestStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    public void readOrderOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.
                get("/orders/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void readOrderNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                get("/orders/{id}", 2))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    public void updateOrderOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.
                put("/orders/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedOrderJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void updateOrderNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                put("/orders/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedOrderJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    public void updateOrderBadRequestStatus() {
        mockMvc.perform(MockMvcRequestBuilders.
                put("/orders/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    public void deleteOrderOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/orders/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void deleteClientNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/orders/{id}", 2))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
