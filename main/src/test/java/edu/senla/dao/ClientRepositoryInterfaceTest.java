package edu.senla.dao;


import edu.senla.entity.Client;
import edu.senla.entity.Order;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class ClientRepositoryInterfaceTest {

    private static final Logger LOG = LoggerFactory.getLogger(ClientRepositoryInterfaceTest.class);

    @Autowired
    private ClientRepositoryInterface clientRepository;

    private Client createdClient;
    private Client invalidClient;

    private int clientId;
    private String clientFirstName;
    private String clientLastName;
    private String clientPhone;
    private String clientEmail;
    private String clientAdderess;
    private List<Order> clientsOrders;

    @Before
    public void setup() {
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

        client.setFirstName(clientFirstName);
        client.setLastName(clientLastName);
        client.setPhone(clientPhone);
        client.setEmail(clientEmail);
        client.setAddress(clientAdderess);
        client.setOrders(clientsOrders);

        createdClient = clientRepository.save(client);
    }

}


