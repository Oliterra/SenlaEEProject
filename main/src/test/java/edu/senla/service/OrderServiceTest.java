package edu.senla.service;

import edu.senla.dao.ClientRepositoryInterface;
import edu.senla.dao.OrderRepositoryInterface;
import edu.senla.entity.Client;
import edu.senla.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepositoryInterface orderRepository;

    @Mock
    private ClientRepositoryInterface clientRepository;

    @Spy
    private ModelMapper mapper;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private Client client;

    private int orderId;
    private String orderPaymentType;
    private String orderStatus;

    @BeforeEach
    void setup() {
        orderId = 1;
        orderPaymentType = "by card";
        orderStatus = "new";

        order = new Order();

        order.setId(orderId);
        order.setStatus(orderStatus);
        order.setPaymentType(orderPaymentType);

        client = new Client();
        client.setId(1);
    }

}