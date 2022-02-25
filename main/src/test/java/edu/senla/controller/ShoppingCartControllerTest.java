package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.impl.ShoppingCartControllerImpl;
import edu.senla.dao.UserRepository;
import edu.senla.dao.DishRepository;
import edu.senla.dao.RoleRepository;
import edu.senla.model.dto.ContainerComponentsDTO;
import edu.senla.model.dto.ShoppingCartDTO;
import edu.senla.model.entity.User;
import edu.senla.model.entity.Dish;
import edu.senla.model.enums.DishType;
import edu.senla.model.enums.Roles;
import edu.senla.service.ClientService;
import edu.senla.service.OrderService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ShoppingCartControllerTest {

    @Autowired
    private ShoppingCartControllerImpl shoppingCartController;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @SpyBean
    private OrderService orderService;

    @SpyBean
    private UserRepository userRepository;

    @SpyBean
    private DishRepository dishRepository;

    @SpyBean
    private RoleRepository roleRepository;

    private User user;
    private Dish meat;
    private Dish garnish;
    private Dish salad;
    private Dish sauce;

    @SneakyThrows
    @BeforeEach
    void creteClientToOperateWith() {
        user = new User();
        user.setFirstName("user");
        user.setLastName("user");
        user.setEmail("user");
        user.setPhone("user");
        user.getRoles().add(roleRepository.getByName(Roles.ROLE_USER.toString()));
        userRepository.save(user);

        meat = new Dish();
        meat.setType(DishType.MEAT);
        meat.setName("meat");
        dishRepository.save(meat);

        garnish = new Dish();
        garnish.setType(DishType.GARNISH);
        garnish.setName("garnish");
        dishRepository.save(garnish);

        salad = new Dish();
        salad.setType(DishType.SALAD);
        salad.setName("salad");
        dishRepository.save(salad);

        sauce = new Dish();
        sauce.setType(DishType.SAUCE);
        sauce.setName("sauce");
        dishRepository.save(sauce);
    }

    @SneakyThrows
    @Test
    void testMakeOrderUnauthorizedStatus() {
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
        String shoppingCartJson = mapper.writeValueAsString(shoppingCartDTO);
        when(clientService.getCurrentClientId()).thenReturn(user.getId());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/shoppingCart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(shoppingCartJson))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        verify(clientService, never()).getCurrentClientId();
        verify(orderService, never()).checkIncomingOrderDataAndCreateIfItIsCorrect(any(Long.class), any());
        assertNull(user.getAddress());
    }

    @SneakyThrows
    @WithMockUser(roles = {"COURIER"})
    @Test
    void testMakeOrderForbiddenStatus() {
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
        String shoppingCartJson = mapper.writeValueAsString(shoppingCartDTO);
        when(clientService.getCurrentClientId()).thenReturn(user.getId());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/shoppingCart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(shoppingCartJson))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(clientService, never()).getCurrentClientId();
        verify(orderService, never()).checkIncomingOrderDataAndCreateIfItIsCorrect(any(Long.class), any());
        assertNull(user.getAddress());
    }

    @SneakyThrows
    @WithMockUser(roles = {"USER"})
    @Test
    void testMakeOrderWithInvalidPaymentTypeBadRequestStatus() {
        ContainerComponentsDTO containerComponentsDTO = new ContainerComponentsDTO();
        containerComponentsDTO.setTypeOfContainer("S");
        containerComponentsDTO.setMeat(meat.getId());
        containerComponentsDTO.setGarnish(garnish.getId());
        containerComponentsDTO.setSalad(salad.getId());
        containerComponentsDTO.setSauce(sauce.getId());
        List<ContainerComponentsDTO> containerComponentsDTOS = new ArrayList<>();
        containerComponentsDTOS.add(containerComponentsDTO);
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
        shoppingCartDTO.setContainers(containerComponentsDTOS);
        shoppingCartDTO.setPaymentType("wrong");
        shoppingCartDTO.setAddress("some address");
        String shoppingCartJson = mapper.writeValueAsString(shoppingCartDTO);
        when(clientService.getCurrentClientId()).thenReturn(user.getId());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/shoppingCart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(shoppingCartJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, times(1)).getCurrentClientId();
        verify(orderService, times(1)).checkIncomingOrderDataAndCreateIfItIsCorrect(any(Long.class), any());
        assertNull(user.getAddress());
    }

    @SneakyThrows
    @WithMockUser(roles = {"USER"})
    @Test
    void testMakeOrderWithInvalidContainerNameBadRequestStatus() {
        ContainerComponentsDTO containerComponentsDTO = new ContainerComponentsDTO();
        containerComponentsDTO.setTypeOfContainer("wrong");
        containerComponentsDTO.setMeat(meat.getId());
        containerComponentsDTO.setGarnish(garnish.getId());
        containerComponentsDTO.setSalad(salad.getId());
        containerComponentsDTO.setSauce(sauce.getId());
        List<ContainerComponentsDTO> containerComponentsDTOS = new ArrayList<>();
        containerComponentsDTOS.add(containerComponentsDTO);
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
        shoppingCartDTO.setContainers(containerComponentsDTOS);
        shoppingCartDTO.setPaymentType("by card to courier");
        shoppingCartDTO.setAddress("some address");
        String shoppingCartJson = mapper.writeValueAsString(shoppingCartDTO);
        when(clientService.getCurrentClientId()).thenReturn(user.getId());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/shoppingCart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(shoppingCartJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, times(1)).getCurrentClientId();
        verify(orderService, times(1)).checkIncomingOrderDataAndCreateIfItIsCorrect(any(Long.class), any());
        assertNull(user.getAddress());
    }

    @SneakyThrows
    @WithMockUser(roles = {"USER"})
    @Test
    void testMakeOrderWithInvalidContainerComponentsBadRequestStatus() {
        ContainerComponentsDTO containerComponentsDTO = new ContainerComponentsDTO();
        containerComponentsDTO.setTypeOfContainer("S");
        containerComponentsDTO.setMeat(999999999);
        containerComponentsDTO.setGarnish(999999999);
        containerComponentsDTO.setSalad(999999999);
        containerComponentsDTO.setSauce(999999999);
        List<ContainerComponentsDTO> containerComponentsDTOS = new ArrayList<>();
        containerComponentsDTOS.add(containerComponentsDTO);
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
        shoppingCartDTO.setContainers(containerComponentsDTOS);
        shoppingCartDTO.setPaymentType("by card to courier");
        shoppingCartDTO.setAddress("some address");
        String shoppingCartJson = mapper.writeValueAsString(shoppingCartDTO);
        when(clientService.getCurrentClientId()).thenReturn(user.getId());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/shoppingCart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(shoppingCartJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, times(1)).getCurrentClientId();
        verify(orderService, times(1)).checkIncomingOrderDataAndCreateIfItIsCorrect(any(Long.class), any());
        assertNull(user.getAddress());
    }

    @SneakyThrows
    @WithMockUser(roles = {"USER"})
    @Test
    void testMakeOrderOkStatus() {
        ContainerComponentsDTO containerComponentsDTO = new ContainerComponentsDTO();
        containerComponentsDTO.setTypeOfContainer("S");
        containerComponentsDTO.setMeat(meat.getId());
        containerComponentsDTO.setGarnish(garnish.getId());
        containerComponentsDTO.setSalad(salad.getId());
        containerComponentsDTO.setSauce(sauce.getId());
        List<ContainerComponentsDTO> containerComponentsDTOS = new ArrayList<>();
        containerComponentsDTOS.add(containerComponentsDTO);
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
        shoppingCartDTO.setContainers(containerComponentsDTOS);
        shoppingCartDTO.setPaymentType("by card to courier");
        shoppingCartDTO.setAddress("some address");
        String shoppingCartJson = mapper.writeValueAsString(shoppingCartDTO);
        when(clientService.getCurrentClientId()).thenReturn(user.getId());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/shoppingCart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(shoppingCartJson))
                .andDo(print())
                .andExpect(status().isOk());
        verify(clientService, times(1)).getCurrentClientId();
        verify(orderService, times(1)).checkIncomingOrderDataAndCreateIfItIsCorrect(any(Long.class), any());
        assertEquals("some address", user.getAddress());
    }

}