package edu.senla.service;

import edu.senla.dao.DishRepositoryInterface;
import edu.senla.entity.Dish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class DishServiceTest {

    @Mock
    private DishRepositoryInterface dishRepository;

    @Spy
    private ModelMapper mapper;

    @InjectMocks
    private DishService dishService;

    private Dish dish;

    private int dishId;
    private String dishType;
    private String dishName;

    @BeforeEach
    void setup() {
        dishId = 1;
        dishType = "TestType";
        dishName = "TestName";

        dish = new Dish();

        dish.setId(dishId);
        dish.setDishType(dishType);
        dish.setName(dishName);
    }

}
