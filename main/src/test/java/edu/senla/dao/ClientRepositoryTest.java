package edu.senla.dao;

import edu.senla.config.DatabaseConfig;
import edu.senla.entity.Client;
import edu.senla.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = {DatabaseConfig.class,
        Client.class, ClientRepository.class})
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    private Client createdClient;
    private Client invalidClient;

    private int clientId;
    private String clientFirstName;
    private String clientLastName;
    private String clientPhone;
    private String clientEmail;
    private String clientAdderess;
    private List<Order> clientsOrders;

    @BeforeEach
    void setup() {
        clientId = 1;
        clientFirstName = "TestFirstName";
        clientLastName = "TestLastName";
        clientPhone = "+375333333333";
        clientEmail = "test@test.com";
        clientAdderess = "TestAdress";

        clientsOrders = new ArrayList<>();
        Order order = new Order();
        order.setPaymentType("by card");
        clientsOrders.add(order);

        Client client = new Client();

        client.setId(clientId);
        client.setFirstName(clientFirstName);
        client.setLastName(clientLastName);
        client.setPhone(clientPhone);
        client.setEmail(clientEmail);
        client.setAddress(clientAdderess);
        client.setOrders(clientsOrders);

        createdClient = clientRepository.create(client);
    }

    @Test
    void createClient() {
        assertEquals(clientId, createdClient.getId());
        assertEquals(clientFirstName, createdClient.getFirstName());
        assertEquals(clientLastName, createdClient.getLastName());
    }

    @Test
    void createNullClient() {
        assertThrows(IllegalArgumentException.class, () -> clientRepository.create(invalidClient));
    }

    @Test
    void readClient() {
        Client readClient = clientRepository.read(createdClient.getId());
        assertEquals(clientId, readClient.getId());
        assertEquals(clientFirstName, readClient.getFirstName());
        assertEquals(clientLastName, readClient.getLastName());
    }

    @Test
    void readNullClient() {
        assertThrows(NullPointerException.class, () -> clientRepository.read(invalidClient.getId()));
    }

    @Test
    void updateClient() {
        Client clientToUpdate = clientRepository.read(createdClient.getId());

        String newFirstName = "Another first name";
        String newLastName = "Another last name";
        clientToUpdate.setFirstName(newFirstName);
        clientToUpdate.setLastName(newLastName);

        Client updatedClient = clientRepository.update(clientToUpdate);

        assertEquals(newFirstName, updatedClient.getFirstName());
        assertEquals(newLastName, updatedClient.getLastName());
    }

    @Test
    void updateNullClient() {
        assertThrows(IllegalArgumentException.class, () -> clientRepository.update(invalidClient));
    }

    @Test
    void deleteClient() {
        clientRepository.delete(createdClient.getId());
        Client deletedClient = clientRepository.read(createdClient.getId());
        assertNull(deletedClient);
    }

    @Test
    void deleteNullClient() {
        assertThrows(NullPointerException.class, () -> clientRepository.delete(invalidClient.getId()));
    }

    @Test
    void getClientByEmail() {
        Client receivedClient = clientRepository.getClientByEmail(clientEmail);
        assertEquals(createdClient, receivedClient);
    }

    @Test
    void getNullClientByEmail() {
        assertThrows(NullPointerException.class, () -> clientRepository.getClientByEmail(invalidClient.getEmail()));
    }

    @Test
    void getByIdWithOrders() {
        Client clientWithOrders = clientRepository.getByIdWithOrders(createdClient.getId());
        assertEquals(clientsOrders, clientWithOrders.getOrders());
    }

    @Test
    void getNullClientByIdWithOrders() {
        assertThrows(NullPointerException.class, () -> clientRepository.getByIdWithOrders(invalidClient.getId()));
    }

}
