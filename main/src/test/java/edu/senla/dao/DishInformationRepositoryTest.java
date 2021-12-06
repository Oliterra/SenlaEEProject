package edu.senla.dao;

import edu.senla.config.DatabaseConfig;
import edu.senla.entity.DishInformation;
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
        DishInformation.class, DishInformationRepository.class})
class DishInformationRepositoryTest {

    @Autowired
    private DishInformationRepository dishInformationRepository;

    private DishInformation createdDishInformation;
    private DishInformation invalidDishInformation;

    private int dishInformationId;
    private String dishInformationDescription;

    @BeforeEach
    void setup() {
        dishInformationId = 1;
        dishInformationDescription = "300 g";

        DishInformation dishInformation = new DishInformation();

        dishInformation.setId(dishInformationId);
        dishInformation.setDescription(dishInformationDescription);

        createdDishInformation = dishInformationRepository.create(dishInformation);
    }

    @Test
    void createDishInformation() {
        assertEquals(dishInformationId, createdDishInformation.getId());
        assertEquals(dishInformationDescription, createdDishInformation.getDescription());
    }

    @Test
    void createNullDishInformation() {
        assertThrows(IllegalArgumentException.class, () -> dishInformationRepository.create(invalidDishInformation));
    }

    @Test
    void readDishInformation() {
        DishInformation readDishInformation = dishInformationRepository.read(createdDishInformation.getId());
        assertEquals(dishInformationId, readDishInformation.getId());
        assertEquals(dishInformationDescription, readDishInformation.getDescription());
    }

    @Test
    void readNullDishInformation() {
        assertThrows(NullPointerException.class, () -> dishInformationRepository.read(invalidDishInformation.getId()));
    }

    @Test
    void updateDishInformation() {
        DishInformation dishInformationToUpdate = dishInformationRepository.read(createdDishInformation.getId());

        String newDescription = "Another description";
        dishInformationToUpdate.setDescription(newDescription);

        DishInformation updatedDishInformation = dishInformationRepository.update(dishInformationToUpdate);

        assertEquals(newDescription, updatedDishInformation.getDescription());
    }

    @Test
    void updateNullDishInformation() {
        assertThrows(IllegalArgumentException.class, () -> dishInformationRepository.update(invalidDishInformation));
    }

    @Test
    void deleteDishInformation() {
        dishInformationRepository.delete(createdDishInformation.getId());
        DishInformation deletedDishInformation = dishInformationRepository.read(createdDishInformation.getId());
        assertNull(deletedDishInformation);
    }

    @Test
    void deleteNullDishInformation() {
        assertThrows(NullPointerException.class, () -> dishInformationRepository.delete(invalidDishInformation.getId()));
    }

}