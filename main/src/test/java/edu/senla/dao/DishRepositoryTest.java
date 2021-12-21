package edu.senla.dao;

import edu.senla.entity.Dish;
import edu.senla.entity.DishInformation;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
/*@ContextConfiguration(classes = {DatabaseConfig.class,
        Dish.class, DishRepository.class})*/
public class DishRepositoryTest {

    @Autowired
    private DishRepository dishRepository;

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
        dishInformation.setCookingDate(LocalDate.MAX);

        Dish dish = new Dish();

        dish.setId(dishId);
        dish.setDishType(dishType);
        dish.setName(dishName);
        dish.setDishInformation(dishInformation);

        createdDish = dishRepository.create(dish);
    }

    @Test
    public void createDish() {
        assertEquals(dishId, createdDish.getId());
        assertEquals(dishType, createdDish.getDishType());
        assertEquals(dishName, createdDish.getName());
    }

    @Test
    public void createNullDish() {
        assertThrows(IllegalArgumentException.class, () -> dishRepository.create(invalidDish));
    }

    @Test
    public void readDish() {
        Dish readDish = dishRepository.read(createdDish.getId());
        assertEquals(dishId, readDish.getId());
        assertEquals(dishType, readDish.getDishType());
        assertEquals(dishName, readDish.getName());
    }

    @Test
    public void readNullDish() {
        assertThrows(NullPointerException.class, () -> dishRepository.read(invalidDish.getId()));
    }

    @Test
    public void updateDish() {
        Dish dishToUpdate = dishRepository.read(createdDish.getId());

        String newType = "AnotherType";
        String newName = "AnotherName";
        dishToUpdate.setDishType(newType);
        dishToUpdate.setName(newName);

        Dish updatedDish = dishRepository.update(dishToUpdate);

        assertEquals(newType, updatedDish.getDishType());
        assertEquals(newName, updatedDish.getName());
    }

    @Test
    public void updateNullDish() {
        assertThrows(IllegalArgumentException.class, () -> dishRepository.update(invalidDish));
    }

    @Test
    public void deleteDish() {
        dishRepository.delete(createdDish.getId());
        Dish deletedDish = dishRepository.read(createdDish.getId());
        assertNull(deletedDish);
    }

    @Test
    public void deleteNullDish() {
        assertThrows(NullPointerException.class, () -> dishRepository.delete(invalidDish.getId()));
    }

    @Test
    public void getDishByName() {
        Dish receivedDish = dishRepository.getDishByName(dishName);
        assertEquals(createdDish, receivedDish);
    }

    @Test
    public void getNullDishByName() {
        assertThrows(NullPointerException.class, () -> dishRepository.getDishByName(invalidDish.getName()));
    }

    @Test
    public void getByIdWithFullInformation() {
        Dish dishWithFullInfo = dishRepository.getByIdWithFullInformation(createdDish.getId());
        assertEquals(dishInformation, dishWithFullInfo.getDishInformation());
    }

    @Test
    public void getNullDishByIdWithFullInformation() {
        assertThrows(NullPointerException.class, () -> dishRepository.getByIdWithFullInformation(invalidDish.getId()));
    }

}
