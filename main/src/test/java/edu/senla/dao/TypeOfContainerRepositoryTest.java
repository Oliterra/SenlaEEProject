package edu.senla.dao;

import edu.senla.entity.TypeOfContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class TypeOfContainerRepositoryTest {

    @Autowired
    private TypeOfContainerRepositoryInterface typeOfContainerRepository;

    private TypeOfContainer createdTypeOfContainer;
    private TypeOfContainer invalidTypeOfContainer;

    private int typeOfContainerCaloricContent;
    private String typeOfContainerName;
    private int typeOfContainerPrice;

    @BeforeEach
    public void setup() {
        typeOfContainerCaloricContent = 1000;
        typeOfContainerName = "TestName";
        typeOfContainerPrice = 20;

        TypeOfContainer typeOfContainer = new TypeOfContainer();

        typeOfContainer.setNumberOfCalories(typeOfContainerCaloricContent);
        typeOfContainer.setName(typeOfContainerName);
        typeOfContainer.setPrice(typeOfContainerPrice);

        createdTypeOfContainer = typeOfContainerRepository.save(typeOfContainer);
    }

}
