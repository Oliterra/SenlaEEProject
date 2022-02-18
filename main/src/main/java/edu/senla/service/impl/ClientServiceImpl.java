package edu.senla.service.impl;

import edu.senla.dao.ClientRepository;
import edu.senla.dao.ContainerRepository;
import edu.senla.dao.OrderRepository;
import edu.senla.dao.RoleRepository;
import edu.senla.exeption.BadRequest;
import edu.senla.exeption.ConflictBetweenData;
import edu.senla.exeption.NotFound;
import edu.senla.model.dto.*;
import edu.senla.model.entity.Client;
import edu.senla.model.entity.Container;
import edu.senla.model.entity.Order;
import edu.senla.model.entity.Role;
import edu.senla.model.enums.CRUDOperations;
import edu.senla.model.enums.OrderStatus;
import edu.senla.model.enums.Roles;
import edu.senla.service.ClientService;
import edu.senla.service.ContainerService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
public class ClientServiceImpl extends AbstractService implements ClientService {

    private final ContainerService containerService;
    private final ContainerRepository containerRepository;
    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    public List<ClientMainInfoDTO> getAllClients(int pages) {
        log.info("Getting all couriers");
        Page<Client> clients = clientRepository.findAll(PageRequest.of(0, pages, Sort.by("lastName").descending()));
        return clients.stream().map(c -> modelMapper.map(c, ClientMainInfoDTO.class)).toList();
    }

    public List<AdminInfoDTO> getAllAdmins(int pages) {
        log.info("Getting all users with the administrator role");
        Role adminRole = roleRepository.getByName(Roles.ROLE_ADMIN.toString());
        List<Client> admins = clientRepository.getAllByRole(adminRole, PageRequest.of(0, pages, Sort.by("lastName").descending()));
        return admins.stream().map(a -> modelMapper.map(a, AdminInfoDTO.class)).toList();
    }

    public List<ClientOrderInfoDTO> getAllOrdersOfClient(long clientId) {
        Client client = getClientIfExists(clientId, CRUDOperations.READ);
        log.info("Requested order history for the client {} {}", client.getFirstName(), client.getLastName());
        return orderRepository.getAllByClient(client, PageRequest.of(0, 10, Sort.by("date").descending())).stream()
                .filter(o -> o.getStatus().equals(OrderStatus.COMPLETED_LATE) || o.getStatus().equals(OrderStatus.COMPLETED_ON_TIME))
                .map(this::formClientOrderInfoDTO)
                .toList();
    }

    public void grantAdministratorRights(long id) {
        ClientRoleInfoDTO clientRoleInfoDTO = getClientRole(id);
        if (isUserAdmin(clientRoleInfoDTO)) {
            log.error("The attempt to assign administrator rights to the user failed because user {} {} is already an admin", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName());
            throw new BadRequest("User is already an admin");
        }
        log.info("Endowment with administrator rights of the user {} {} (current role: {})", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName(), clientRoleInfoDTO.getRole());
        setRoleToClient(id, Roles.ROLE_ADMIN);
        log.info("User {} {} is an administrator now", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName());
    }

    public void revokeAdministratorRights(long id) {
        ClientRoleInfoDTO clientRoleInfoDTO = getClientRole(id);
        if (!isUserAdmin(clientRoleInfoDTO)) {
            log.error("The attempt to assign administrator rights to the user failed because user {} {} was not an admin", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName());
            throw new BadRequest("User is not an admin");
        }
        log.info("Depriving the user {} {} of administrator rights", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName());
        setRoleToClient(id, Roles.ROLE_USER);
        log.info("User {} {} is a user now", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName());
    }

    @SneakyThrows
    public void createClient(String registrationRequestJson) {
        RegistrationRequestDTO newClientDTO = objectMapper.readValue(registrationRequestJson, RegistrationRequestDTO.class);
        log.info("A new client wants to register in the service: " + newClientDTO);
        checkClientName(newClientDTO.getFirstName(), CRUDOperations.CREATE);
        checkClientName(newClientDTO.getLastName(), CRUDOperations.CREATE);
        checkClientEmail(newClientDTO.getEmail(), CRUDOperations.CREATE);
        checkClientPhone(newClientDTO.getPhone(), CRUDOperations.CREATE);
        findPossibleDuplicate(newClientDTO);
        checkClientPasswordConfirmation(newClientDTO);
        ClientFullInfoDTO clientFullInfoDTO = formFullClientRegistrationInformation(newClientDTO);
        Client client = clientRepository.save(modelMapper.map(clientFullInfoDTO, Client.class));
        log.info("A new client is registered in the service: " + client);
    }

    public ClientMainInfoDTO getClient(long id) {
        log.info("Getting client with id {}: ", id);
        checkClientExistent(id, CRUDOperations.READ);
        ClientMainInfoDTO clientMainInfoDTO = modelMapper.map(clientRepository.getById(id), ClientMainInfoDTO.class);
        log.info("Client found: {}: ", clientMainInfoDTO);
        return clientMainInfoDTO;
    }

    @SneakyThrows
    public ClientFullInfoDTO getClientByUsernameAndPassword(String authRequestJson) {
        AuthRequestDTO authRequestDTO = objectMapper.readValue(authRequestJson, AuthRequestDTO.class);
        String username = authRequestDTO.getUsername();
        String password = authRequestDTO.getPassword();
        try {
            Client client = clientRepository.getByUsername(username);
            if (!passwordEncoder.matches(password, client.getPassword())) throw new BadRequest();
            return modelMapper.map(client, ClientFullInfoDTO.class);
        } catch (RuntimeException exception) {
            log.error("No user found with username {} and password {}", username, password);
            throw new NotFound("Invalid username or password");
        }
    }

    public long getCurrentClientId() {
        String clientUsername = getCurrentClientUsername();
        return clientRepository.getByUsername(clientUsername).getId();
    }

    @SneakyThrows
    public void updateClient(long id, String updatedClientJson) {
        ClientMainInfoDTO clientDTO = objectMapper.readValue(updatedClientJson, ClientMainInfoDTO.class);
        Client clientToUpdate = getClientIfExists(id, CRUDOperations.UPDATE);
        log.info("Updating client with id {} with new data {}: ", id, clientDTO);
        checkClientName(clientDTO.getFirstName(), CRUDOperations.UPDATE);
        checkClientName(clientDTO.getLastName(), CRUDOperations.UPDATE);
        checkClientEmail(clientDTO.getEmail(), CRUDOperations.UPDATE);
        checkClientPhone(clientDTO.getPhone(), CRUDOperations.UPDATE);
        findPossibleDuplicate(clientDTO);
        Client updatedClient = modelMapper.map(clientDTO, Client.class);
        Client clientWithNewParameters = updateClientsOptions(clientToUpdate, updatedClient);
        clientRepository.save(clientWithNewParameters);
        log.info("Client with id {} successfully updated", id);
    }

    public void deleteClient(long id) {
        log.info("Deleting client with id: {}", id);
        checkClientExistent(id, CRUDOperations.DELETE);
        clientRepository.deleteById(id);
        log.info("Client with id {} successfully deleted", id);
    }

    private Client getClientIfExists(long id, CRUDOperations operation) {
        if (!clientRepository.existsById(id)) {
            log.info("The attempt to {} a client failed, there is no client with id {}", operation.toString().toLowerCase(), id);
            throw new NotFound("There is no client with id " + id);
        }
        return clientRepository.getById(id);
    }

    private void checkClientExistent(long id, CRUDOperations operation) {
        if (!clientRepository.existsById(id)) {
            log.info("The attempt to {} a client failed, there is no client with id {}", operation.toString().toLowerCase(), id);
            throw new NotFound("There is no client with id " + id);
        }
    }

    private ClientRoleInfoDTO getClientRole(long id) {
        Client client = getClientIfExists(id, CRUDOperations.READ);
        return modelMapper.map(client, ClientRoleInfoDTO.class);
    }

    private void findPossibleDuplicate(RegistrationRequestDTO registrationRequestDTO) {
        String possibleDuplicate = isClientExists(registrationRequestDTO);
        if (possibleDuplicate != null) {
            log.info("The attempt to register a client failed, because client with this {} already exists", possibleDuplicate);
            throw new ConflictBetweenData("A user with this " + possibleDuplicate + " already exists ");
        }
    }

    private void findPossibleDuplicate(ClientMainInfoDTO clientMainInfoDTO) {
        String possibleDuplicate = isClientExists(clientMainInfoDTO);
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
        if (clientRepository.getByEmail(registrationRequestDTO.getEmail()) != null) return "email";
        if (clientRepository.getByPhone(registrationRequestDTO.getPhone()) != null) return "phone";
        if (clientRepository.getByUsername(registrationRequestDTO.getUsername()) != null) return "username";
        return null;
    }

    private String isClientExists(ClientMainInfoDTO clientMainInfoDTO) {
        if (clientRepository.getByEmail(clientMainInfoDTO.getEmail()) != null) return "email";
        if (clientRepository.getByPhone(clientMainInfoDTO.getPhone()) != null) return "phone";
        return null;
    }

    private boolean isUserAdmin(ClientRoleInfoDTO clientRoleInfoDTO) {
        Role adminRole = roleRepository.getByName(Roles.ROLE_ADMIN.toString());
        return clientRoleInfoDTO.getRole().equals(adminRole);
    }

    public void setRoleToClient(long id, Roles role) {
        Client client = getClientIfExists(id, CRUDOperations.UPDATE);
        Role roleToSet = switch (role) {
            case ROLE_ADMIN -> roleRepository.getByName(Roles.ROLE_ADMIN.toString());
            case ROLE_USER -> roleRepository.getByName(Roles.ROLE_USER.toString());
            default -> throw new BadRequest("There is no such role");
        };
        client.setRole(roleToSet);
        clientRepository.save(client);
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

    private Client updateClientsOptions(Client client, Client updatedClient) {
        client.setFirstName(updatedClient.getFirstName());
        client.setLastName(updatedClient.getLastName());
        client.setEmail(updatedClient.getEmail());
        client.setPhone(updatedClient.getPhone());
        client.setAddress(updatedClient.getAddress());
        return client;
    }

    private String getCurrentClientUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private ClientFullInfoDTO formFullClientRegistrationInformation(RegistrationRequestDTO registrationRequestDTO) {
        ClientFullInfoDTO clientFullInfoDTO = modelMapper.map(registrationRequestDTO, ClientFullInfoDTO.class);
        clientFullInfoDTO.setUsername(registrationRequestDTO.getUsername());
        clientFullInfoDTO.setRole(roleRepository.getByName(Roles.ROLE_USER.toString()));
        clientFullInfoDTO.setPassword(passwordEncoder.encode(registrationRequestDTO.getPassword()));
        return clientFullInfoDTO;
    }

    private ClientOrderInfoDTO formClientOrderInfoDTO(Order order) {
        List<Container> containers = containerRepository.findAllByOrderId(order.getId());
        List<ContainerComponentsNamesDTO> containersCourierInfoDTOs = containerRepository.findAllByOrderId(order.getId()).stream()
                .map(containerService::mapFromContainerEntityToContainerComponentsNamesDTO).toList();
        ClientOrderInfoDTO clientOrderInfoDTO = new ClientOrderInfoDTO();
        clientOrderInfoDTO.setDate(order.getDate());
        clientOrderInfoDTO.setTime(order.getTime());
        clientOrderInfoDTO.setCourierName(order.getCourier().getFirstName() + " " + order.getCourier().getLastName());
        clientOrderInfoDTO.setPaymentType(order.getPaymentType().toString().toLowerCase(Locale.ROOT));
        clientOrderInfoDTO.setOrderDeliveredOnTime(order.getStatus().equals(OrderStatus.COMPLETED_ON_TIME));
        clientOrderInfoDTO.setOrderCost(containerService.calculateTotalOrderCost(containers));
        clientOrderInfoDTO.setContainers(containersCourierInfoDTOs);
        return clientOrderInfoDTO;
    }
}

