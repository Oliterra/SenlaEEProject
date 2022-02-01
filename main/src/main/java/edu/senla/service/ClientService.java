package edu.senla.service;

import ch.qos.logback.classic.Logger;
import edu.senla.dao.ClientRepositoryInterface;
import edu.senla.dao.ContainerRepositoryInterface;
import edu.senla.dao.OrderRepositoryInterface;
import edu.senla.dao.RoleRepositoryInterface;
import edu.senla.dto.*;
import edu.senla.entity.Client;
import edu.senla.entity.Container;
import edu.senla.entity.Order;
import edu.senla.entity.Role;
import edu.senla.enums.CRUDOperations;
import edu.senla.enums.OrderStatus;
import edu.senla.enums.Roles;
import edu.senla.exeptions.BadRequest;
import edu.senla.exeptions.ConflictBetweenData;
import edu.senla.exeptions.NotFound;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import edu.senla.service.serviceinterface.ContainerServiceInterface;
import edu.senla.service.serviceinterface.ValidationServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
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
public class ClientService implements ClientServiceInterface {

    private final ContainerServiceInterface containerService;

    private final ValidationServiceInterface validationService;

    private final ContainerRepositoryInterface containerRepository;

    private final ClientRepositoryInterface clientRepository;

    private final RoleRepositoryInterface roleRepository;

    private final OrderRepositoryInterface orderRepository;

    private final PasswordEncoder passwordEncoder;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(ClientService.class);

    private final ModelMapper mapper;

    public List<ClientMainInfoDTO> getAllClients() {
        LOG.info("Getting all couriers");
        Page<Client> clients = clientRepository.findAll(PageRequest.of(0, 10, Sort.by("lastName").descending()));
        return clients.stream().map(c -> mapper.map(c, ClientMainInfoDTO.class)).toList();
    }

    public List<AdminInfoDTO> getAllAdmins() {
        LOG.info("Getting all users with the administrator role");
        Role adminRole = roleRepository.getByName(Roles.ROLE_ADMIN.toString());
        List<Client> admins = clientRepository.getAllByRole(adminRole, PageRequest.of(0, 10, Sort.by("lastName").descending()));
        return admins.stream().map(a -> mapper.map(a, AdminInfoDTO.class)).toList();
    }

    public List<ClientOrderInfoDTO> getAllOrdersOfClient(long clientId) {
        Client client = getClientIfExists(clientId, CRUDOperations.READ);
        LOG.info("Requested order history for the client {} {}", client.getFirstName(), client.getLastName());
        return orderRepository.getAllByClient(client, PageRequest.of(0, 10, Sort.by("date").descending())).stream()
                .filter(o -> o.getStatus().equals("completed late") || o.getStatus().equals("completed on time"))
                .map(this::formClientOrderInfoDTO)
                .toList();
    }

    public void grantAdministratorRights(long id) {
        ClientRoleInfoDTO clientRoleInfoDTO = getClientRole(id);
        if (isUserAdmin(clientRoleInfoDTO)){
            LOG.error("The attempt to assign administrator rights to the user failed because user {} {} is already an admin", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName());
            throw new BadRequest("User is already an admin");
        }
        LOG.info("Endowment with administrator rights of the user {} {} (current role: {})", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName(), clientRoleInfoDTO.getRole());
        setRoleToClient(id, Roles.ROLE_ADMIN);
        LOG.info("User {} {} is an administrator now", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName());
    }

    public void revokeAdministratorRights(long id) {
        ClientRoleInfoDTO clientRoleInfoDTO = getClientRole(id);
        if (!isUserAdmin(clientRoleInfoDTO)){
            LOG.error("The attempt to assign administrator rights to the user failed because user {} {} was not an admin", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName());
            throw new BadRequest("User is not an admin");
        }
        LOG.info("Depriving the user {} {} of administrator rights", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName());
        setRoleToClient(id, Roles.ROLE_USER);
        LOG.info("User {} {} is a user now", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName());
    }

    public void createClient(RegistrationRequestDTO newClientDTO) {
        LOG.info("A new client wants to register in the service: " + newClientDTO);
        checkClientName(newClientDTO.getFirstName(), CRUDOperations.CREATE);
        checkClientName(newClientDTO.getLastName(), CRUDOperations.CREATE);
        checkClientEmail(newClientDTO.getEmail(), CRUDOperations.CREATE);
        checkClientPhone(newClientDTO.getPhone(), CRUDOperations.CREATE);
        findPossibleDuplicate(newClientDTO);
        checkClientPasswordConfirmation(newClientDTO);
        ClientFullInfoDTO clientFullInfoDTO = formFullClientRegistrationInformation(newClientDTO);
        Client client = clientRepository.save(mapper.map(clientFullInfoDTO, Client.class));
        LOG.info("A new client is registered in the service: " + client);
    }

    public ClientMainInfoDTO getClient(long id) {
        LOG.info("Getting client with id {}: ", id);
        Client client = getClientIfExists(id, CRUDOperations.READ);
        ClientMainInfoDTO clientMainInfoDTO = mapper.map(clientRepository.getById(id), ClientMainInfoDTO.class);
        LOG.info("Client found: {}: ", clientMainInfoDTO);
        return clientMainInfoDTO;
    }

    public ClientFullInfoDTO getClientByUsernameAndPassword(String username, String password) {
        try {
            Client client = clientRepository.getByUsername(username);
            if(!passwordEncoder.matches(password, client.getPassword())) throw new BadRequest();
            return mapper.map(client, ClientFullInfoDTO.class);
        } catch (RuntimeException exception) {
            LOG.error("No user found with username {} and password {}", username, password);
            throw new NotFound("Invalid username or password");
        }
    }

    public long getCurrentClientId() {
        String clientUsername = getCurrentClientUsername();
        return clientRepository.getByUsername(clientUsername).getId();
    }

    public void updateClient(long id, ClientMainInfoDTO clientDTO) {
        Client clientToUpdate = getClientIfExists(id, CRUDOperations.UPDATE);
        LOG.info("Updating client with id {} with new data {}: ", id, clientDTO);
        checkClientName(clientDTO.getFirstName(), CRUDOperations.UPDATE);
        checkClientName(clientDTO.getLastName(), CRUDOperations.UPDATE);
        checkClientEmail(clientDTO.getEmail(), CRUDOperations.UPDATE);
        checkClientPhone(clientDTO.getPhone(), CRUDOperations.UPDATE);
        findPossibleDuplicate(clientDTO);
        Client updatedClient = mapper.map(clientDTO, Client.class);
        Client clientWithNewParameters = updateClientsOptions(clientToUpdate, updatedClient);
        clientRepository.save(clientWithNewParameters);
        LOG.info("Client with id {} successfully updated", id);
    }

    public void deleteClient(long id) {
        LOG.info("Deleting client with id: {}", id);
        Client clientToDelete = getClientIfExists(id, CRUDOperations.DELETE);
        clientRepository.deleteById(id);
        LOG.info("Client with id {} successfully deleted", id);
    }

    private Client getClientIfExists(long id, CRUDOperations operation) {
        if (!clientRepository.existsById(id)) {
            LOG.info("The attempt to {} a client failed, there is no client with id {}", operation.toString().toLowerCase(), id);
            throw new NotFound("There is no client with id " + id);
        }
        return clientRepository.getById(id);
    }

    private ClientRoleInfoDTO getClientRole(long id) {
        Client client = getClientIfExists(id, CRUDOperations.READ);
        return mapper.map(client, ClientRoleInfoDTO.class);
    }

    private void findPossibleDuplicate(RegistrationRequestDTO registrationRequestDTO) {
        String possibleDuplicate = isClientExists(registrationRequestDTO);
        if (possibleDuplicate != null) {
            LOG.info("The attempt to register a client failed, because client with this {} already exists", possibleDuplicate);
            throw new ConflictBetweenData("A user with this " + possibleDuplicate + " already exists ");
        }
    }

    private void findPossibleDuplicate(ClientMainInfoDTO clientMainInfoDTO) {
        String possibleDuplicate = isClientExists(clientMainInfoDTO);
        if (possibleDuplicate != null) {
            LOG.info("The attempt to update a client failed, because client with this {} already exists", possibleDuplicate);
            throw new ConflictBetweenData("A user with this " + possibleDuplicate + " already exists ");
        }
    }

    private void checkClientPasswordConfirmation(RegistrationRequestDTO registrationRequestDTO) {
        if (!(registrationRequestDTO.getPassword()).equals(registrationRequestDTO.getPasswordConfirm())) {
            LOG.info("The attempt to register a client failed, because passwords {} and {} do not match",
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
        Role roleToSet;
        switch (role) {
            case ROLE_ADMIN:
                roleToSet = roleRepository.getByName(Roles.ROLE_ADMIN.toString());
                break;
            case ROLE_USER:
                roleToSet = roleRepository.getByName(Roles.ROLE_USER.toString());
                break;
            default: throw new BadRequest("There is no such role");
        }
        client.setRole(roleToSet);
        clientRepository.save(client);
    }

    private void checkClientName(String name, CRUDOperations operation) {
        if (!validationService.isNameCorrect(name)) {
            LOG.error("The attempt to {} a client failed, a client name {} contains invalid characters", operation.toString().toLowerCase(), name);
            throw new BadRequest("Name " + name + " contains invalid characters");
        }
        if (!validationService.isNameLengthValid(name)) {
            LOG.error("The attempt to {} a client failed, a client name {} is to short", operation.toString().toLowerCase(), name);
            throw new BadRequest("Name " + name + " is too short");
        }
    }

    private void checkClientEmail(String email, CRUDOperations operation) {
        if (!validationService.isEmailCorrect(email)) {
            LOG.error("The attempt to {} a client failed, a client email {} is invalid", operation.toString().toLowerCase(), email);
            throw new BadRequest("Email " + email + " is invalid");
        }
    }

    private void checkClientPhone(String phone, CRUDOperations operation) {
        if (!validationService.isPhoneCorrect(phone)) {
            LOG.error("The attempt to {} a client failed, a client phone {} is invalid", operation.toString().toLowerCase(), phone);
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
        ClientFullInfoDTO clientFullInfoDTO = mapper.map(registrationRequestDTO, ClientFullInfoDTO.class);
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

