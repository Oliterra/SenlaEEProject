package edu.senla.service.impl;

import edu.senla.dao.ContainerRepository;
import edu.senla.dao.OrderRepository;
import edu.senla.dao.RoleRepository;
import edu.senla.dao.UserRepository;
import edu.senla.exeption.BadRequest;
import edu.senla.exeption.ConflictBetweenData;
import edu.senla.exeption.NotFound;
import edu.senla.model.dto.*;
import edu.senla.model.entity.Container;
import edu.senla.model.entity.Order;
import edu.senla.model.entity.Role;
import edu.senla.model.entity.User;
import edu.senla.model.enums.CRUDOperations;
import edu.senla.model.enums.OrderStatus;
import edu.senla.model.enums.Roles;
import edu.senla.service.ContainerService;
import edu.senla.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;

@Transactional
@RequiredArgsConstructor
@Service
@Log4j2
public class UserServiceImpl extends AbstractService implements UserService {

    private final ContainerService containerService;
    private final ContainerRepository containerRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserMainInfoDTO> getAllClients(int pages) {
        log.info("Getting all couriers");
        //Page<User> clients = userRepository.findAll(PageRequest.of(0, pages, Sort.by("lastName").descending()));
        Page<User> clients = null;
        return clients.stream().map(c -> modelMapper.map(c, UserMainInfoDTO.class)).toList();
    }

    public List<AdminInfoDTO> getAllAdmins(int pages) {
        log.info("Getting all users with the administrator role");
        Role adminRole = roleRepository.getByName(Roles.ROLE_ADMIN.toString());
        //List<User> admins = userRepository.getAllByRoles(adminRole, PageRequest.of(0, pages, Sort.by("lastName").descending()));
        List<User> admins = null;
        return admins.stream().map(a -> modelMapper.map(a, AdminInfoDTO.class)).toList();
    }

    public List<UserOrderInfoDTO> getAllOrdersOfClient(long clientId) {
        User user = getClientIfExists(clientId, CRUDOperations.READ);
        log.info("Requested order history for the user {} {}", user.getFirstName(), user.getLastName());
        /*return orderRepository.getAllByUser(user, PageRequest.of(0, 10, Sort.by("date").descending())).stream()
                .filter(o -> o.getStatus().equals(OrderStatus.COMPLETED_LATE) || o.getStatus().equals(OrderStatus.COMPLETED_ON_TIME))
                .map(this::formClientOrderInfoDTO)
                .toList();*/
        return null;
    }

    public void grantAdministratorRights(long id) {
        UserRoleInfoDTO userRoleInfoDTO = getClientRole(id);
        if (isUserAdmin(userRoleInfoDTO)) {
            log.error("The attempt to assign administrator rights to the user failed because user {} {} is already an admin", userRoleInfoDTO.getFirstName(), userRoleInfoDTO.getLastName());
            throw new BadRequest("User is already an admin");
        }
        log.info("Endowment with administrator rights of the user {} {} (current role: {})", userRoleInfoDTO.getFirstName(), userRoleInfoDTO.getLastName(), userRoleInfoDTO.getRole());
        setRoleToClient(id, Roles.ROLE_ADMIN);
        log.info("User {} {} is an administrator now", userRoleInfoDTO.getFirstName(), userRoleInfoDTO.getLastName());
    }

    public void revokeAdministratorRights(long id) {
        UserRoleInfoDTO userRoleInfoDTO = getClientRole(id);
        if (!isUserAdmin(userRoleInfoDTO)) {
            log.error("The attempt to assign administrator rights to the user failed because user {} {} was not an admin", userRoleInfoDTO.getFirstName(), userRoleInfoDTO.getLastName());
            throw new BadRequest("User is not an admin");
        }
        log.info("Depriving the user {} {} of administrator rights", userRoleInfoDTO.getFirstName(), userRoleInfoDTO.getLastName());
        setRoleToClient(id, Roles.ROLE_USER);
        log.info("User {} {} is a user now", userRoleInfoDTO.getFirstName(), userRoleInfoDTO.getLastName());
    }

    @SneakyThrows
    public void createClient(String registrationRequestJson) {
        RegistrationRequestDTO newClientDTO = objectMapper.readValue(registrationRequestJson, RegistrationRequestDTO.class);
        log.info("A new user wants to register in the service: " + newClientDTO);
        checkClientName(newClientDTO.getFirstName(), CRUDOperations.CREATE);
        checkClientName(newClientDTO.getLastName(), CRUDOperations.CREATE);
        checkClientEmail(newClientDTO.getEmail(), CRUDOperations.CREATE);
        checkClientPhone(newClientDTO.getPhone(), CRUDOperations.CREATE);
        findPossibleDuplicate(newClientDTO);
        checkClientPasswordConfirmation(newClientDTO);
        UserFullInfoDTO userFullInfoDTO = formFullClientRegistrationInformation(newClientDTO);
        User user = userRepository.save(modelMapper.map(userFullInfoDTO, User.class));
        log.info("A new user is registered in the service: " + user);
    }

    public UserMainInfoDTO getClient(long id) {
        log.info("Getting client with id {}: ", id);
        checkClientExistent(id, CRUDOperations.READ);
        UserMainInfoDTO userMainInfoDTO = modelMapper.map(userRepository.getById(id), UserMainInfoDTO.class);
        log.info("User found: {}: ", userMainInfoDTO);
        return userMainInfoDTO;
    }

    @SneakyThrows
    public UserFullInfoDTO getClientByUsernameAndPassword(String authRequestJson) {
        AuthRequestDTO authRequestDTO = objectMapper.readValue(authRequestJson, AuthRequestDTO.class);
        String username = authRequestDTO.getUsername();
        String password = authRequestDTO.getPassword();
        try {
            User user = userRepository.getByUsername(username);
            if (!passwordEncoder.matches(password, user.getPassword())) throw new BadRequest();
            return modelMapper.map(user, UserFullInfoDTO.class);
        } catch (RuntimeException exception) {
            log.error("No user found with username {} and password {}", username, password);
            throw new NotFound("Invalid username or password");
        }
    }

    public long getCurrentClientId() {
        String clientUsername = getCurrentClientUsername();
        return userRepository.getByUsername(clientUsername).getId();
    }

    @SneakyThrows
    public void updateClient(long id, String updatedClientJson) {
        UserMainInfoDTO clientDTO = objectMapper.readValue(updatedClientJson, UserMainInfoDTO.class);
        User userToUpdate = getClientIfExists(id, CRUDOperations.UPDATE);
        log.info("Updating client with id {} with new data {}: ", id, clientDTO);
        checkClientName(clientDTO.getFirstName(), CRUDOperations.UPDATE);
        checkClientName(clientDTO.getLastName(), CRUDOperations.UPDATE);
        checkClientEmail(clientDTO.getEmail(), CRUDOperations.UPDATE);
        checkClientPhone(clientDTO.getPhone(), CRUDOperations.UPDATE);
        findPossibleDuplicate(clientDTO);
        User updatedUser = modelMapper.map(clientDTO, User.class);
        User userWithNewParameters = updateClientsOptions(userToUpdate, updatedUser);
        userRepository.save(userWithNewParameters);
        log.info("User with id {} successfully updated", id);
    }

    public void deleteClient(long id) {
        log.info("Deleting client with id: {}", id);
        checkClientExistent(id, CRUDOperations.DELETE);
        userRepository.deleteById(id);
        log.info("User with id {} successfully deleted", id);
    }

    private User getClientIfExists(long id, CRUDOperations operation) {
        if (!userRepository.existsById(id)) {
            log.info("The attempt to {} a client failed, there is no client with id {}", operation.toString().toLowerCase(), id);
            throw new NotFound("There is no client with id " + id);
        }
        return userRepository.getById(id);
    }

    private void checkClientExistent(long id, CRUDOperations operation) {
        if (!userRepository.existsById(id)) {
            log.info("The attempt to {} a client failed, there is no client with id {}", operation.toString().toLowerCase(), id);
            throw new NotFound("There is no client with id " + id);
        }
    }

    private UserRoleInfoDTO getClientRole(long id) {
        User user = getClientIfExists(id, CRUDOperations.READ);
        return modelMapper.map(user, UserRoleInfoDTO.class);
    }

    private void findPossibleDuplicate(RegistrationRequestDTO registrationRequestDTO) {
        String possibleDuplicate = isClientExists(registrationRequestDTO);
        if (possibleDuplicate != null) {
            log.info("The attempt to register a client failed, because client with this {} already exists", possibleDuplicate);
            throw new ConflictBetweenData("A user with this " + possibleDuplicate + " already exists ");
        }
    }

    private void findPossibleDuplicate(UserMainInfoDTO userMainInfoDTO) {
        String possibleDuplicate = isClientExists(userMainInfoDTO);
        if (possibleDuplicate != null) {
            log.info("The attempt to update a client failed, because client with this {} already exists", possibleDuplicate);
            throw new ConflictBetweenData("A user with this " + possibleDuplicate + " already exists ");
        }
    }

    private void checkClientPasswordConfirmation(RegistrationRequestDTO registrationRequestDTO) {
        if (!(registrationRequestDTO.getPassword()).equals(registrationRequestDTO.getPasswordConfirm())) {
            log.info("The attempt to register a client failed, because passwords {} and {} do not match",
                    registrationRequestDTO.getPassword(), registrationRequestDTO.getPasswordConfirm());
            throw new BadRequest("Passwords " + registrationRequestDTO.getPassword() + " and " + registrationRequestDTO.getPasswordConfirm() + " do not match");
        }
    }

    private String isClientExists(RegistrationRequestDTO registrationRequestDTO) {
        if (userRepository.getByEmail(registrationRequestDTO.getEmail()) != null) return "email";
        if (userRepository.getByPhone(registrationRequestDTO.getPhone()) != null) return "phone";
        if (userRepository.getByUsername(registrationRequestDTO.getUsername()) != null) return "username";
        return null;
    }

    private String isClientExists(UserMainInfoDTO userMainInfoDTO) {
        if (userRepository.getByEmail(userMainInfoDTO.getEmail()) != null) return "email";
        if (userRepository.getByPhone(userMainInfoDTO.getPhone()) != null) return "phone";
        return null;
    }

    private boolean isUserAdmin(UserRoleInfoDTO userRoleInfoDTO) {
        Role adminRole = roleRepository.getByName(Roles.ROLE_ADMIN.toString());
        return userRoleInfoDTO.getRole().equals(adminRole);
    }

    public void setRoleToClient(long id, Roles role) {
        User user = getClientIfExists(id, CRUDOperations.UPDATE);
        Role roleToSet = switch (role) {
            case ROLE_ADMIN -> roleRepository.getByName(Roles.ROLE_ADMIN.toString());
            case ROLE_USER -> roleRepository.getByName(Roles.ROLE_USER.toString());
            default -> throw new BadRequest("There is no such role");
        };
        user.getRoles().add(roleToSet);
        userRepository.save(user);
    }

    private void checkClientName(String name, CRUDOperations operation) {
        if (!validationService.isNameCorrect(name)) {
            log.error("The attempt to {} a client failed, a client name {} contains invalid characters", operation.toString().toLowerCase(), name);
            throw new BadRequest("Name " + name + " contains invalid characters");
        }
        if (!validationService.isNameLengthValid(name)) {
            log.error("The attempt to {} a client failed, a client name {} is to short", operation.toString().toLowerCase(), name);
            throw new BadRequest("Name " + name + " is too short");
        }
    }

    private void checkClientEmail(String email, CRUDOperations operation) {
        if (!validationService.isEmailCorrect(email)) {
            log.error("The attempt to {} a client failed, a client email {} is invalid", operation.toString().toLowerCase(), email);
            throw new BadRequest("Email " + email + " is invalid");
        }
    }

    private void checkClientPhone(String phone, CRUDOperations operation) {
        if (!validationService.isPhoneCorrect(phone)) {
            log.error("The attempt to {} a client failed, a client phone {} is invalid", operation.toString().toLowerCase(), phone);
            throw new BadRequest("Phone " + phone + " is invalid");
        }
    }

    private User updateClientsOptions(User user, User updatedUser) {
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        user.setAddress(updatedUser.getAddress());
        return user;
    }

    private String getCurrentClientUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private UserFullInfoDTO formFullClientRegistrationInformation(RegistrationRequestDTO registrationRequestDTO) {
        UserFullInfoDTO userFullInfoDTO = modelMapper.map(registrationRequestDTO, UserFullInfoDTO.class);
        userFullInfoDTO.setUsername(registrationRequestDTO.getUsername());
        userFullInfoDTO.setRole(roleRepository.getByName(Roles.ROLE_USER.toString()));
        userFullInfoDTO.setPassword(passwordEncoder.encode(registrationRequestDTO.getPassword()));
        return userFullInfoDTO;
    }

    private UserOrderInfoDTO formClientOrderInfoDTO(Order order) {
        List<Container> containers = containerRepository.findAllByOrderId(order.getId());
        List<ContainerComponentsNamesDTO> containersCourierInfoDTOs = containerRepository.findAllByOrderId(order.getId()).stream()
                .map(containerService::mapFromContainerEntityToContainerComponentsNamesDTO).toList();
        UserOrderInfoDTO userOrderInfoDTO = new UserOrderInfoDTO();
        userOrderInfoDTO.setDate(order.getDate());
        userOrderInfoDTO.setTime(order.getTime());
        userOrderInfoDTO.setCourierName(order.getCourier().getFirstName() + " " + order.getCourier().getLastName());
        userOrderInfoDTO.setPaymentType(order.getPaymentType().toString().toLowerCase(Locale.ROOT));
        userOrderInfoDTO.setOrderDeliveredOnTime(order.getStatus().equals(OrderStatus.COMPLETED_ON_TIME));
        userOrderInfoDTO.setOrderCost(containerService.calculateTotalOrderCost(containers));
        userOrderInfoDTO.setContainers(containersCourierInfoDTOs);
        return userOrderInfoDTO;
    }
}

