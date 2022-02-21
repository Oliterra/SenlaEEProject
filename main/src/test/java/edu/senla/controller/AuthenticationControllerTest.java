package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.impl.AuthenticationControllerImpl;
import edu.senla.dao.UserRepository;
import edu.senla.dao.CourierRepository;
import edu.senla.dao.RoleRepository;
import edu.senla.model.dto.CourierAuthRequestDTO;
import edu.senla.model.entity.User;
import edu.senla.model.entity.Courier;
import edu.senla.model.enums.Roles;
import edu.senla.security.JwtProvider;
import edu.senla.service.impl.ClientServiceImpl;
import edu.senla.service.impl.CourierServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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
class AuthenticationControllerTest {

    @Autowired
    private AuthenticationControllerImpl authenticationController;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @SpyBean
    private ClientServiceImpl clientService;

    @SpyBean
    private CourierServiceImpl courierService;

    @SpyBean
    private JwtProvider jwtProvider;

    @SpyBean
    private UserRepository userRepository;

    @SpyBean
    private CourierRepository courierRepository;

    @SpyBean
    private RoleRepository roleRepository;

    private User user;

    private Courier courier;

    @SneakyThrows
    @BeforeEach
    void creteUsersToOperateWith() {
        user = new User();
        user.setFirstName("user");
        user.setLastName("user");
        user.setEmail("user");
        user.setPhone("user");
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user"));
        user.getRoles().add(roleRepository.getByName(Roles.ROLE_USER.toString()));
        User user1 = userRepository.save(user);
        System.out.println(user1);

        courier = new Courier();
        courier.setFirstName("courier");
        courier.setLastName("courier");
        courier.setPhone("courier");
        courier.setPassword(passwordEncoder.encode("courier"));
        courierRepository.save(courier);
    }

    /*@SneakyThrows
    @Test
    void testAuthenticateClientWhenJsonIsIncorrectBadRequestStatus() {
        String incorrectJson = "incorrectJson";
        mockMvc.perform(MockMvcRequestBuilders
                .get("/authentication/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(incorrectJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, never()).getClientByUsernameAndPassword(any(), any());
        verify(jwtProvider, never()).generateToken(any());
    }

    @SneakyThrows
    @Test
    void testAuthenticateNonExistentClientNonFoundStatus() {
        AuthRequestDTO authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setUsername("notExistentClient");
        authRequestDTO.setPassword("notExistentClient");
        String authRequestJson = mapper.writeValueAsString(authRequestDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/authentication/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authRequestJson))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(clientService, times(1)).getClientByUsernameAndPassword(any(), any());
        verify(jwtProvider, never()).generateToken(any());
    }

    @SneakyThrows
    @Test
    void testAuthenticateClientOkStatus() {
        AuthRequestDTO authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setUsername("user");
        authRequestDTO.setPassword("user");
        String authRequestJson = mapper.writeValueAsString(authRequestDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/authentication/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authRequestJson))
                .andDo(print())
                .andExpect(status().isOk());
        verify(clientService, times(1)).getClientByUsernameAndPassword(any(), any());
        verify(jwtProvider, times(1)).generateToken(any());
    }*/

    @SneakyThrows
    @Test
    void testAuthenticateCourierWhenJsonIsIncorrectBadRequestStatus() {
        String incorrectJson = "incorrectJson";
        mockMvc.perform(MockMvcRequestBuilders
                .get("/authentication/couriers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(incorrectJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(courierService, never()).getCourierByPhoneAndPassword(any());
        verify(jwtProvider, never()).generateToken(any());
    }

    @SneakyThrows
    @Test
    void testAuthenticateNonExistentCourierNonFoundStatus() {
        CourierAuthRequestDTO authRequestDTO = new CourierAuthRequestDTO();
        authRequestDTO.setPhone("notExistentCourier");
        authRequestDTO.setPassword("notExistentCourier");
        String authRequestJson = mapper.writeValueAsString(authRequestDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/authentication/couriers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authRequestJson))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(courierService, times(1)).getCourierByPhoneAndPassword(any());
        verify(jwtProvider, never()).generateToken(any());
    }

    @SneakyThrows
    @Test
    void testAuthenticateCourierOkStatus() {
        CourierAuthRequestDTO authRequestDTO = new CourierAuthRequestDTO();
        authRequestDTO.setPhone("courier");
        authRequestDTO.setPassword("courier");
        String authRequestJson = mapper.writeValueAsString(authRequestDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/authentication/couriers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authRequestJson))
                .andDo(print())
                .andExpect(status().isOk());
        verify(courierService, times(1)).getCourierByPhoneAndPassword(any());
        verify(jwtProvider, times(1)).generateToken(any());
    }

}