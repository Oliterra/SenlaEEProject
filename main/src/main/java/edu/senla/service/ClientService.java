package edu.senla.service;

import edu.senla.dao.ClientRepositoryInterface;
import edu.senla.dao.ContainerRepositoryInterface;
import edu.senla.dao.OrderRepositoryInterface;
import edu.senla.dao.RoleRepositoryInterface;
import edu.senla.dto.*;
import edu.senla.entity.Client;
import edu.senla.entity.Container;
import edu.senla.entity.Order;
import edu.senla.entity.Role;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import edu.senla.service.serviceinterface.ContainerServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ClientService implements ClientServiceInterface {

    private final ContainerServiceInterface containerService;

    private final ContainerRepositoryInterface containerRepository;

    private final ClientRepositoryInterface clientRepository;

    private final RoleRepositoryInterface roleRepository;

    private final OrderRepositoryInterface orderRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper mapper;

    public List<ClientMainInfoDTO> getAllClients() {
        Page<Client> clients = clientRepository.findAll(PageRequest.of(0, 10, Sort.by("lastName").descending()));
        return clients.stream().map(c -> mapper.map(c, ClientMainInfoDTO.class)).toList();
    }

    public List<AdminInfoDTO> getAllAdmins(){
        Role adminRole = roleRepository.getByName("ROLE_ADMIN");
        List<Client> admins = clientRepository.getAllByRole(adminRole, PageRequest.of(0, 10, Sort.by("lastName").descending()));
        return admins.stream().map(a -> mapper.map(a, AdminInfoDTO.class)).toList();
    }

    public List<ClientOrderInfoDTO> getAllOrdersOfClient(long clientId) {
        Client client = clientRepository.getById(clientId);
        return orderRepository.getAllByClient(client, PageRequest.of(0, 10, Sort.by("date").descending())).stream()
                .filter(o -> o.getStatus().equals("completed late") || o.getStatus().equals("completed on time"))
                .map(o -> formClientOrderInfoDTO(o))
                .toList();
    }

    public void createClient(RegistrationRequestDTO newClientDTO) {
        ClientFullInfoDTO clientFullInfoDTO = formFullClientInformation(newClientDTO);
        clientRepository.save(mapper.map(clientFullInfoDTO, Client.class));
    }

    public ClientMainInfoDTO getClient(long id) {
        try {
            return mapper.map(clientRepository.getById(id), ClientMainInfoDTO.class);
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public ClientFullInfoDTO getClientByUsernameAndPassword(String username, String password) {
        try {
            Client client = clientRepository.getByUsername(username);
            return passwordEncoder.matches(password, client.getPassword()) ? mapper.map(client, ClientFullInfoDTO.class) : null;
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public ClientBasicInfoDTO getClientBasicInfo(long id) {
        try {
            return mapper.map(clientRepository.getById(id), ClientBasicInfoDTO.class);
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public ClientRoleInfoDTO getClientRole(long id) {
        try {
            return mapper.map(clientRepository.getById(id), ClientRoleInfoDTO.class);
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public long getCurrentClientId() {
        String clientUsername = getCurrentClientUsername();
        return clientRepository.getByUsername(clientUsername).getId();
    }

    public void updateClient(long id, ClientMainInfoDTO clientDTO) {
        Client updatedClient = mapper.map(clientDTO, Client.class);
        Client clientToUpdate = clientRepository.getById(id);
        Client clientWithNewParameters = updateClientsOptions(clientToUpdate, updatedClient);
        clientRepository.save(clientWithNewParameters);
    }

    public void deleteClient(long id) {
        clientRepository.deleteById(id);
    }

    public String isClientExists(RegistrationRequestDTO registrationRequestDTO) {
        if (clientRepository.getByEmail(registrationRequestDTO.getEmail()) != null) return "email";
        if (clientRepository.getByPhone(registrationRequestDTO.getPhone()) != null) return "phone";
        if (clientRepository.getByUsername(registrationRequestDTO.getUsername()) != null) return "username";
        return null;
    }

    public String isClientExists(ClientMainInfoDTO clientMainInfoDTO) {
        if (clientRepository.getByEmail(clientMainInfoDTO.getEmail()) != null) return "email";
        if (clientRepository.getByPhone(clientMainInfoDTO.getPhone()) != null) return "phone";
        return null;
    }

    public boolean isClientExists(long id) {
        return clientRepository.existsById(id);
    }

    public boolean isUserAdmin(ClientRoleInfoDTO clientRoleInfoDTO) {
        Role adminRole = roleRepository.getByName("ROLE_ADMIN");
        return clientRoleInfoDTO.getRole().equals(adminRole);
    }

    public void grantAdministratorRights(long id) {
        Client client = clientRepository.getById(id);
        Role adminRole = roleRepository.getByName("ROLE_ADMIN");
        client.setRole(adminRole);
        clientRepository.save(client);
    }

    public void revokeAdministratorRights(long id) {
        Client client = clientRepository.getById(id);
        Role userRole = roleRepository.getByName("ROLE_USER");
        client.setRole(userRole);
        clientRepository.save(client);
    }

    private Client updateClientsOptions(Client client, Client updatedClient) {
        client.setFirstName(updatedClient.getFirstName());
        client.setLastName(updatedClient.getLastName());
        client.setEmail(updatedClient.getEmail());
        client.setPhone(updatedClient.getPhone());
        client.setAddress(updatedClient.getAddress());
        return client;
    }

    private String getCurrentClientUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private ClientFullInfoDTO formFullClientInformation(RegistrationRequestDTO registrationRequestDTO) {
        ClientFullInfoDTO clientFullInfoDTO = mapper.map(registrationRequestDTO, ClientFullInfoDTO.class);
        clientFullInfoDTO.setUsername(registrationRequestDTO.getUsername());
        clientFullInfoDTO.setRole(roleRepository.getByName("ROLE_USER"));
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
        clientOrderInfoDTO.setPaymentType(order.getPaymentType());
        clientOrderInfoDTO.setOrderDeliveredOnTime(order.getStatus().equals("completed on time"));
        clientOrderInfoDTO.setOrderCost(containerService.calculateTotalOrderCost(containers));
        clientOrderInfoDTO.setContainers(containersCourierInfoDTOs);
        return clientOrderInfoDTO;
    }

}

