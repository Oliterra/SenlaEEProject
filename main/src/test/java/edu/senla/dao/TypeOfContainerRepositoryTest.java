package edu.senla.dao;

import edu.senla.config.DatabaseConfig;
import edu.senla.entity.TypeOfContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = {DatabaseConfig.class,
        TypeOfContainer.class, TypeOfContainerRepository.class})
class TypeOfContainerRepositoryTest {

    @Autowired
    private TypeOfContainerRepository typeOfContainerRepository;

    private TypeOfContainer createdTypeOfContainer;
    private TypeOfContainer invalidTypeOfContainer;

    private int typeOfContainerCaloricContent;
    private String typeOfContainerName;
    private int typeOfContainerPrice;

    @BeforeEach
    void setup() {
        typeOfContainerCaloricContent = 1000;
        typeOfContainerName = "TestName";
        typeOfContainerPrice = 20;

        TypeOfContainer typeOfContainer = new TypeOfContainer();

        typeOfContainer.setNumberOfCalories(typeOfContainerCaloricContent);
        typeOfContainer.setName(typeOfContainerName);
        typeOfContainer.setPrice(typeOfContainerPrice);

        createdTypeOfContainer = typeOfContainerRepository.create(typeOfContainer);
    }

    @Test
    void createTypeOfContainer() {
        assertEquals(typeOfContainerCaloricContent, createdTypeOfContainer.getNumberOfCalories());
        assertEquals(typeOfContainerName, createdTypeOfContainer.getName());
        assertEquals(typeOfContainerPrice, createdTypeOfContainer.getPrice());
    }

    @Test
    void createNullTypeOfContainer() {
        assertThrows(IllegalArgumentException.class, () -> typeOfContainerRepository.create(invalidTypeOfContainer));
    }

    @Test
    void readTypeOfContainer() {
        TypeOfContainer readTypeOfContainer = typeOfContainerRepository.read(createdTypeOfContainer.getNumberOfCalories());
        assertEquals(typeOfContainerCaloricContent, readTypeOfContainer.getNumberOfCalories());
        assertEquals(typeOfContainerName, readTypeOfContainer.getName());
        assertEquals(typeOfContainerPrice, readTypeOfContainer.getPrice());
    }

    @Test
    void readNullTypeOfContainer() {
        assertThrows(NullPointerException.class, () -> typeOfContainerRepository.read(invalidTypeOfContainer.getNumberOfCalories()));
    }

    @Test
    void updateTypeOfContainer() {
        TypeOfContainer typeOfContainerToUpdate = typeOfContainerRepository.read(createdTypeOfContainer.getNumberOfCalories());

        String newName = "Another name";
        int newPrice = 30;
        typeOfContainerToUpdate.setName(newName);
        typeOfContainerToUpdate.setPrice(newPrice);

        TypeOfContainer updatedTypeOfContainer = typeOfContainerRepository.update(typeOfContainerToUpdate);

        assertEquals(newName, updatedTypeOfContainer.getName());
        assertEquals(newPrice, updatedTypeOfContainer.getPrice());
    }

    @Test
    void updateNullTypeOfContainer() {
        assertThrows(IllegalArgumentException.class, () -> typeOfContainerRepository.update(invalidTypeOfContainer));
    }

    @Test
    void deleteTypeOfContainer() {
        typeOfContainerRepository.delete(createdTypeOfContainer.getNumberOfCalories());
        TypeOfContainer deletedTypeOfContainer = typeOfContainerRepository.read(createdTypeOfContainer.getNumberOfCalories());
        assertNull(deletedTypeOfContainer);
    }

    @Test
    void deleteNullTypeOfContainer() {
        assertThrows(NullPointerException.class, () -> typeOfContainerRepository.delete(invalidTypeOfContainer.getNumberOfCalories()));
    }

}
