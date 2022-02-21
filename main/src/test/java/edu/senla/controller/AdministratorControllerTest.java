package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.impl.AdministratorControllerImpl;
import edu.senla.dao.UserRepository;
import edu.senla.dao.RoleRepository;
import edu.senla.model.entity.User;
import edu.senla.model.enums.Roles;
import edu.senla.service.impl.ClientServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
public class AdministratorControllerTest {

    @Autowired
    private AdministratorControllerImpl administratorController;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private ClientServiceImpl clientService;

    @SpyBean
    private UserRepository userRepository;

    @SpyBean
    private RoleRepository roleRepository;

    private User userAdmin;

    private User userUser;

    @SneakyThrows
    @BeforeEach
    void creteClientsToOperateWith() {
        userAdmin = new User();
        userAdmin.setFirstName("admin");
        userAdmin.setLastName("admin");
        userAdmin.setEmail("admin");
        userAdmin.setPhone("admin");
        userAdmin.getRoles().add(roleRepository.getByName(Roles.ROLE_ADMIN.toString()));
        userRepository.save(userAdmin);

        userUser = new User();
        userUser.setFirstName("user");
        userUser.setLastName("user");
        userUser.setEmail("user");
        userUser.setPhone("user");
        userUser.getRoles().add(roleRepository.getByName(Roles.ROLE_USER.toString()));
        userRepository.save(userUser);
    }

    @SneakyThrows
    @Test
    void testGetAllAdminsUnauthorizedStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/administrators"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        verify(clientService, never()).getAllAdmins(10);
    }

    @SneakyThrows
    @WithMockUser(roles={"USER"})
    @Test
    void testGetAllAdminsForbiddenStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/administrators"))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(clientService, never()).getAllAdmins(10);
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testGetAllAdminsOkStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/administrators"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(clientService, times(1)).getAllAdmins(10);
    }

    @SneakyThrows
    @Test
    void testGrantAdministratorRightsUnauthorizedStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .patch("/administrators/assigning/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        verify(clientService, never()).grantAdministratorRights(any(Long.class));
    }

    @SneakyThrows
    @WithMockUser(roles={"USER"})
    @Test
    void testGrantAdministratorRightsForbiddenStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .patch("/administrators/assigning/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(clientService, never()).grantAdministratorRights(any(Long.class));
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testGrantAdministratorRightsToNotExistentNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .patch("/administrators/assigning/3333333"))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(clientService, times(1)).grantAdministratorRights(any(Long.class));
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testGrantAdministratorRightsWhenUserIsAlreadyAnAdminBadRequestStatus() {
        long idOfClientAdmin = userAdmin.getId();
        mockMvc.perform(MockMvcRequestBuilders
                .patch("/administrators/assigning/{idOfClientAdmin}", idOfClientAdmin))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, times(1)).grantAdministratorRights(any(Long.class));
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testGrantAdministratorRightsOkStatus() {
        long idOfClientUser = userUser.getId();
        mockMvc.perform(MockMvcRequestBuilders
                .patch("/administrators/assigning/{idOfClientUser}", idOfClientUser))
                .andDo(print())
                .andExpect(status().isOk());
        verify(clientService, times(1)).grantAdministratorRights(any(Long.class));
    }

    @SneakyThrows
    @Test
    void testRevokeAdministratorRightsUnauthorizedStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/administrators/deprivation/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
        verify(clientService, never()).revokeAdministratorRights(any(Long.class));
    }

    @SneakyThrows
    @WithMockUser(roles={"USER"})
    @Test
    void testRevokeAdministratorRightsForbiddenStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/administrators/deprivation/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(clientService, never()).revokeAdministratorRights(any(Long.class));
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testRevokeAdministratorRightsToNotExistentNotFoundStatus() {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/administrators/deprivation/3333333"))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(clientService, times(1)).revokeAdministratorRights(any(Long.class));
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testRevokeAdministratorRightsWhenUserIsAlreadyAUserBadRequestStatus() {
        long idOfClientUser = userUser.getId();
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/administrators/deprivation/{idOfClientUser}", idOfClientUser))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(clientService, times(1)).revokeAdministratorRights(any(Long.class));
    }

    @SneakyThrows
    @WithMockUser(roles={"ADMIN"})
    @Test
    void testRevokeAdministratorRightsOkStatus() {
        long idOfClientAdmin = userAdmin.getId();
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/administrators/deprivation/{idOfClientAdmin}", idOfClientAdmin))
                .andDo(print())
                .andExpect(status().isOk());
        verify(clientService, times(1)).revokeAdministratorRights(any(Long.class));
    }

}