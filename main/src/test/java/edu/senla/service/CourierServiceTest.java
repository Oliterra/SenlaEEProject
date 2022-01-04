package edu.senla.service;

import edu.senla.dao.CourierRepositoryInterface;
import edu.senla.entity.Courier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    @Mock
    private CourierRepositoryInterface courierRepository;

    @Spy
    private ModelMapper mapper;

    @InjectMocks
    private CourierService courierService;

    private Courier courier;

    private int courierId;
    private String courierFirstName;
    private String courierLastName;

    @BeforeEach
    void setup() {
        courierId = 1;
        courierFirstName = "TestFirstName";
        courierLastName = "TestLastName";

        courier = new Courier();

        courier.setId(courierId);
        courier.setFirstName(courierFirstName);
        courier.setLastName(courierLastName);
    }

}
