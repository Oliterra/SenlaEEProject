package edu.senla.service;

import edu.senla.dao.DishRepository;
import edu.senla.dto.DishDTO;
import edu.senla.dto.DishInformationDTO;
import edu.senla.entity.Dish;
import edu.senla.entity.DishInformation;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.persistence.NoResultException;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishServiceTest {

    @Mock
    private DishRepository dishRepository;

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

    @Test
    void createDish() {
        when(dishRepository.create(any(Dish.class))).thenReturn(dish);

        DishDTO dishParamsDTO = new DishDTO(dishId, dishName, dishType);
        DishDTO createdDishDTO = dishService.createDish(dishParamsDTO);

        verify(dishRepository, times(1)).create(any());

        Assert.assertEquals(dishId, createdDishDTO.getId());
        Assert.assertEquals(dishName, createdDishDTO.getName());
        Assert.assertEquals(dishType, createdDishDTO.getDishType());
    }

    @Test
    void createNullDish() {
        DishDTO invalidDishDTO = null;
        Assert.assertThrows(IllegalArgumentException.class, () -> dishService.createDish(invalidDishDTO));
    }

    @Test
    void readDish() {
        when(dishRepository.read(any(Integer.class))).thenReturn(dish);

        DishDTO readDishDTO = dishService.readDish(dishId);

        verify(dishRepository, times(1)).read(any());

        Assert.assertEquals(dishId, readDishDTO.getId());
        Assert.assertEquals(dishName, readDishDTO.getName());
        Assert.assertEquals(dishType, readDishDTO.getDishType());
    }

    @Test
    void readNullDish() {
        Assert.assertThrows(IllegalArgumentException.class, () -> dishService.readDish(0));
    }

    @Test
    void updateDish() {
        String newDishType = "Another dish type";
        String newDishName = "Another dish name";

        Dish updatedDish = new Dish();
        updatedDish.setId(dishId);
        updatedDish.setDishType(newDishType);
        updatedDish.setName(newDishName);

        when(dishRepository.update(any(Dish.class))).thenReturn(updatedDish);
        when(dishRepository.read(any(Integer.class))).thenReturn(dish);

        DishDTO updatedDishParamsDTO = new DishDTO(dishId, newDishName, newDishType);
        DishDTO updatedDishDTO = dishService.updateDish(dishId, updatedDishParamsDTO);

        verify(dishRepository, times(1)).update(any());

        Assert.assertEquals(dishId, updatedDishDTO.getId());
        Assert.assertEquals(newDishName, updatedDishDTO.getName());
        Assert.assertEquals(newDishType, updatedDishDTO.getDishType());
    }

    @Test
    void updateNullDish() {
        String newDishType = "Another dish type";
        String newDishName = "Another dish name";
        DishDTO updatedDishParamsDTO = new DishDTO(dishId, newDishName, newDishType);

        DishDTO invalidDishDTO = null;
        Assert.assertThrows(NullPointerException.class, () -> dishService.updateDish(invalidDishDTO.getId(), updatedDishParamsDTO));
    }

    @Test
    void updateDishWithNullParams() {
        DishDTO invalidUpdatedDishParamsDTO = null;

        Assert.assertThrows(IllegalArgumentException.class, () -> dishService.updateDish(dishId, invalidUpdatedDishParamsDTO));
    }

    @Test
    void deleteDish() {
        dishService.deleteDish(dishId);
        verify(dishRepository, times(1)).delete(any());
    }

    @Test
    void deleteNullDish() {
        DishDTO invalidDishDTO = null;
        Assert.assertThrows(NullPointerException.class, () -> dishService.deleteDish(invalidDishDTO.getId()));
    }

    @Test
    void getDishByIdWithFullInformation() {
        DishInformation dishInformation = new DishInformation();
        dishInformation.setId(1);
        dishInformation.setDish(dish);
        dishInformation.setDescription("testDescription");
        dishInformation.setCaloricContent(298);
        dishInformation.setProteins(22);
        dishInformation.setFats(33);
        dishInformation.setCarbohydrates(44);
        dishInformation.setCookingDate(LocalDate.now());
        dishInformation.setExpirationDate(LocalDate.now());

        dish.setDishInformation(dishInformation);
        when(dishRepository.getByIdWithFullInformation(any(Integer.class))).thenReturn(dish);

        DishDTO dishWithFullInfoDTO = dishService.getByIdWithFullInformation(dishId);

        verify(dishRepository, times(1)).getByIdWithFullInformation(any(Integer.class));

        Assert.assertEquals(dishId, dishWithFullInfoDTO.getId());
        Assert.assertEquals(dishName, dishWithFullInfoDTO.getName());
        Assert.assertEquals(dishType, dishWithFullInfoDTO.getDishType());
        Assert.assertEquals(mapper.map(dishInformation, DishInformationDTO.class), dishWithFullInfoDTO.getDishInformation());
    }

    @Test
    void getNullDishByIdWithFullInformation() {
        Assert.assertThrows(IllegalArgumentException.class, () -> dishService.getByIdWithFullInformation(dish.getId()));
    }

    @Test
    void getDishByIdWithNullFullInformation() {
        DishInformation dishInformation = null;
        dish.setDishInformation(dishInformation);

        when(dishRepository.getByIdWithFullInformation(any(Integer.class))).thenReturn(dish);

        DishDTO dishWithFullInfoDTO = dishService.getByIdWithFullInformation(dishId);

        verify(dishRepository, times(1)).getByIdWithFullInformation(any(Integer.class));

        Assert.assertEquals(dishId, dishWithFullInfoDTO.getId());
        Assert.assertEquals(dishName, dishWithFullInfoDTO.getName());
        Assert.assertEquals(dishType, dishWithFullInfoDTO.getDishType());
        Assert.assertNull(dishWithFullInfoDTO.getDishInformation());
    }

    @Test
    void isExistentDishExists() {
        when(dishRepository.getDishByName(any(String.class))).thenReturn(dish);

        DishDTO dishWithNameDTO = new DishDTO();
        dishWithNameDTO.setName(dishName);

        boolean dishExists = dishService.isDishExists(dishWithNameDTO);

        verify(dishRepository, times(1)).getDishByName(any(String.class));

        Assert.assertTrue(dishExists);
    }

    @Test
    void isNonExistentDishExists() {
        when(dishRepository.getDishByName(any(String.class))).thenThrow(new NoResultException());

        DishDTO dishWithNameDTO = new DishDTO();
        dishWithNameDTO.setName(dishName);

        boolean dishExists = dishService.isDishExists(dishWithNameDTO);

        verify(dishRepository, times(1)).getDishByName(any(String.class));

        Assert.assertThrows(NoResultException.class, () -> dishRepository.getDishByName(dishName));
        Assert.assertFalse(dishExists);
    }

    @Test
    void isNullDishExists() {
        DishDTO invalidDishWithNameDTO = null;

        Assert.assertThrows(NullPointerException.class, () -> dishService.isDishExists(invalidDishWithNameDTO));
    }

}