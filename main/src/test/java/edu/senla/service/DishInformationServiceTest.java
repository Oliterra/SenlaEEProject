package edu.senla.service;

import edu.senla.dao.DishInformationRepositoryInterface;
import edu.senla.dao.DishRepositoryInterface;
import edu.senla.entity.Dish;
import edu.senla.entity.DishInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;


@ExtendWith(MockitoExtension.class)
class DishInformationServiceTest {

    @Mock
    private DishInformationRepositoryInterface dishInformationRepositoryInterface;

    @Mock
    private DishRepositoryInterface dishRepository;

    @Spy
    private ModelMapper mapper;

    @InjectMocks
    private DishInformationService dishInformationService;

    private DishInformation dishInformation;
    private Dish dish;

    private int dishInformationId;
    private int dishInformationCaloricContent;
    private String dishInformationDescription;

    @BeforeEach
    void setup() {
        dishInformationId = 1;
        dishInformationCaloricContent = 300;
        dishInformationDescription = "200 g";

        dishInformation = new DishInformation();

        dishInformation.setId(dishInformationId);
        dishInformation.setCaloricContent(300);
        dishInformation.setDescription(dishInformationDescription);

        dish = new Dish();
        dish.setId(1);
    }

}
