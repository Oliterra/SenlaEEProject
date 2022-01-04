package edu.senla.dao;

import edu.senla.entity.Dish;
import edu.senla.entity.DishInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class DishRepositoryTest {

    @Autowired
    private DishRepositoryInterface dishRepository;

    private Dish createdDish;
    private Dish invalidDish;

    private int dishId;
    private String dishType;
    private String dishName;
    private DishInformation dishInformation;

    @BeforeEach
    public void setup() {
        dishId = 1;
        dishType = "TestDishType";
        dishName = "TestDishName";

        dishInformation = new DishInformation();
        dishInformation.setId(1);
        dishInformation.setDescription("300 g");

        Dish dish = new Dish();

        dish.setId(dishId);
        dish.setDishType(dishType);
        dish.setName(dishName);
        dish.setDishInformation(dishInformation);

        createdDish = dishRepository.save(dish);
    }

}
