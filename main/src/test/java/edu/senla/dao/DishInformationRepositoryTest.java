package edu.senla.dao;

import edu.senla.entity.DishInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class DishInformationRepositoryTest {

    @Autowired
    private DishInformationRepositoryInterface dishInformationRepositoryInterface;

    private DishInformation createdDishInformation;
    private DishInformation invalidDishInformation;

    private int dishInformationId;
    private String dishInformationDescription;

    @BeforeEach
    public void setup() {
        dishInformationId = 1;
        dishInformationDescription = "300 g";

        DishInformation dishInformation = new DishInformation();

        dishInformation.setId(dishInformationId);
        dishInformation.setDescription(dishInformationDescription);

        createdDishInformation = dishInformationRepositoryInterface.save(dishInformation);
    }

}
