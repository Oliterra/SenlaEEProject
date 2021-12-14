package edu.senla.service;

import edu.senla.dao.ClientRepository;
import edu.senla.dto.ClientDTO;
import edu.senla.entity.Client;
import edu.senla.entity.Order;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Spy
    private ModelMapper mapper;

    @InjectMocks
    private ClientService clientService;

    private Client client;

    private int clientId;
    private String clientFirstName;
    private String clientLastName;

    @BeforeEach
    void setup() {
        clientId = 1;
        clientFirstName = "TestFirstName";
        clientLastName = "TestLastName";

        client = new Client();

        client.setId(clientId);
        client.setFirstName(clientFirstName);
        client.setLastName(clientLastName);
    }

    @Test
    public void createNullClient() {
        ClientDTO invalidClientDTO = null;
        Assert.assertThrows(IllegalArgumentException.class, () -> clientService.createClient(invalidClientDTO));
    }

    @Test
    public void readClient() {
        when(clientRepository.read(any(Integer.class))).thenReturn(client);

        ClientDTO readClientDTO = clientService.readClient(clientId);

        verify(clientRepository, times(1)).read(any());

        Assert.assertEquals(clientId, readClientDTO.getId());
        Assert.assertEquals(clientFirstName, readClientDTO.getFirstName());
        Assert.assertEquals(clientLastName, readClientDTO.getLastName());
    }

    @Test
    public void readNullClient() {
        Assert.assertThrows(IllegalArgumentException.class, () -> clientService.readClient(0));
    }

    @Test
    public void updateClient() {
        String newFirstName = "Another first name";
        String newLastName = "Another last name";

        Client updatedClient = new Client();
        updatedClient.setId(clientId);
        updatedClient.setFirstName(newFirstName);
        updatedClient.setLastName(newLastName);

        when(clientRepository.update(any(Client.class))).thenReturn(updatedClient);
        when(clientRepository.read(any(Integer.class))).thenReturn(client);

        ClientDTO updatedClientParamsDTO = new ClientDTO(clientId, newFirstName, newLastName);
        ClientDTO updatedClientDTO = clientService.updateClient(clientId, updatedClientParamsDTO);

        verify(clientRepository, times(1)).update(any());

        Assert.assertEquals(clientId, updatedClientDTO.getId());
        Assert.assertEquals(newFirstName, updatedClientDTO.getFirstName());
        Assert.assertEquals(newLastName, updatedClientDTO.getLastName());
    }

    @Test
    public void updateNullClient() {
        String newFirstName = "Another first name";
        String newLastName = "Another last name";
        ClientDTO updatedClientParamsDTO = new ClientDTO(clientId, newFirstName, newLastName);

        ClientDTO invalidClientDTO = null;
        Assert.assertThrows(NullPointerException.class, () -> clientService.updateClient(invalidClientDTO.getId(), updatedClientParamsDTO));
    }

    @Test
    public void updateClientWithNullParams() {
        ClientDTO invalidUpdatedClientParamsDTO = null;

        Assert.assertThrows(IllegalArgumentException.class, () -> clientService.updateClient(clientId, invalidUpdatedClientParamsDTO));
    }

    @Test
    public void deleteClient() {
        clientRepository.delete(clientId);
        verify(clientRepository, times(1)).delete(any());
    }

    @Test
    public void deleteNullClient() {
        ClientDTO invalidClientDTO = null;
        Assert.assertThrows(NullPointerException.class, () -> clientService.deleteClient(invalidClientDTO.getId()));
    }

    @Test
    public void getClientByIdWithOrders() {
        List<Order> ordersList = new ArrayList<>();
        Order order = new Order();
        order.setId(1);
        order.setStatus("new");
        ordersList.add(order);

        client.setOrders(ordersList);
        when(clientRepository.getByIdWithOrders(any(Integer.class))).thenReturn(client);

        ClientDTO clientWithOrdersDTO = clientService.getByIdWithOrders(clientId);

        verify(clientRepository, times(1)).getByIdWithOrders(any(Integer.class));

        Assert.assertEquals(clientId, clientWithOrdersDTO.getId());
        Assert.assertEquals(clientFirstName, clientWithOrdersDTO.getFirstName());
        Assert.assertEquals(clientLastName, clientWithOrdersDTO.getLastName());
        Assert.assertEquals(ordersList, clientWithOrdersDTO.getOrders());
    }

    @Test
    public void getNullClientByIdWithOrders() {
        Assert.assertThrows(IllegalArgumentException.class, () -> clientService.getByIdWithOrders(client.getId()));
    }

    @Test
    public void getClientByIdWithNullOrders() {
        List<Order> ordersList = null;
        client.setOrders(ordersList);

        when(clientRepository.getByIdWithOrders(any(Integer.class))).thenReturn(client);

        ClientDTO clientWithOrdersDTO = clientService.getByIdWithOrders(clientId);

        verify(clientRepository, times(1)).getByIdWithOrders(any(Integer.class));

        Assert.assertEquals(clientId, clientWithOrdersDTO.getId());
        Assert.assertEquals(clientFirstName, clientWithOrdersDTO.getFirstName());
        Assert.assertEquals(clientLastName, clientWithOrdersDTO.getLastName());
        Assert.assertNull(clientWithOrdersDTO.getOrders());
    }

    @Test
    public void isExistentClientExists() {
        String clientsEmail = "test@test.com";
        client.setEmail(clientsEmail);
        when(clientRepository.getClientByEmail(any(String.class))).thenReturn(client);

        ClientDTO clientWithEmailDTO = new ClientDTO();
        clientWithEmailDTO.setEmail(clientsEmail);

        boolean clientExists = clientService.isClientExists(clientWithEmailDTO);

        verify(clientRepository, times(1)).getClientByEmail(any(String.class));

        Assert.assertTrue(clientExists);
    }

    @Test
    public void isNonExistentClientExists() {
        String clientsEmail = "test@test.com";

        when(clientRepository.getClientByEmail(any(String.class))).thenThrow(new NoResultException());

        ClientDTO clientWithEmailDTO = new ClientDTO();
        clientWithEmailDTO.setEmail(clientsEmail);

        boolean clientExists = clientService.isClientExists(clientWithEmailDTO);

        verify(clientRepository, times(1)).getClientByEmail(any(String.class));

        Assert.assertThrows(NoResultException.class, () -> clientRepository.getClientByEmail(clientsEmail));
        Assert.assertFalse(clientExists);
    }

    @Test
    public void isNullClientExists() {
        ClientDTO invalidClientWithEmailDTO = null;

        Assert.assertThrows(NullPointerException.class, () -> clientService.isClientExists(invalidClientWithEmailDTO));
    }

}

