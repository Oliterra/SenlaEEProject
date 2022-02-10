package edu.senla.service;

import edu.senla.dao.DishRepository;
import edu.senla.model.dto.DishDTO;
import edu.senla.model.entity.Dish;
import edu.senla.model.enums.DishType;
import edu.senla.exeption.BadRequest;
import edu.senla.exeption.ConflictBetweenData;
import edu.senla.exeption.NotFound;
import edu.senla.service.impl.DishServiceImpl;
import edu.senla.service.impl.ValidationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishServiceTest {

    @Mock
    private DishRepository dishRepository;

    @Spy
    private ModelMapper mapper;

    @Spy
    private ValidationServiceImpl validationService;

    @InjectMocks
    private DishServiceImpl dishService;

    @Test
    void testGetAllDishes() {
        List<Dish> dishesList = new ArrayList<>();
        Dish dish = new Dish();
        dish.setName("Some name");
        dishesList.add(dish);
        Page<Dish> dishes = new PageImpl<>(dishesList);
        when(dishRepository.findAll(any(Pageable.class))).thenReturn(dishes);
        List<DishDTO> dishDTOS = dishService.getAllDishes(10);
        verify(dishRepository, times(1)).findAll((Pageable) any());
        assertTrue(dishDTOS.size() == 1);
        assertEquals(dish.getName(), dishDTOS.get(0).getName());
    }

    @Test
    void testGetAllDishesWhenThereAreNoDishes() {
        List<Dish> dishesList = new ArrayList<>();
        Page<Dish> dishes = new PageImpl<>(dishesList);
        when(dishRepository.findAll(any(Pageable.class))).thenReturn(dishes);
        List<DishDTO> dishDTOS = dishService.getAllDishes(10);
        verify(dishRepository, times(1)).findAll((Pageable) any());
        assertTrue(dishDTOS.isEmpty());
    }

    /*@Test
    void testCreateAlreadyExistentDish() {
        Dish dish = new Dish();
        DishDTO newDishDTO = new DishDTO();
        newDishDTO.setName("CorrectName");
        newDishDTO.setDishType("meat");
        when(dishRepository.getByName(any(String.class))).thenReturn(dish);
        assertThrows(ConflictBetweenData.class, () ->  dishService.createDish(newDishDTO));
        verify(dishRepository, times(1)).getByName(any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(dishRepository, never()).save(any());
    }

    @Test
    void testCreateDishWithIncorrectSymbolsInName() {
        DishDTO newDishDTO = new DishDTO();
        newDishDTO.setName("@!*%");
        newDishDTO.setDishType("meat");
        assertThrows(BadRequest.class, () ->  dishService.createDish(newDishDTO));
        verify(dishRepository, times(1)).getByName(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(dishRepository, never()).save(any());
    }

    @Test
    void testCreateDishWithTooShortName() {
        DishDTO newDishDTO = new DishDTO();
        newDishDTO.setName("d");
        newDishDTO.setDishType("meat");
        assertThrows(BadRequest.class, () ->  dishService.createDish(newDishDTO));
        verify(dishRepository, times(1)).getByName(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
        verify(dishRepository, never()).save(any());
    }

    @Test
    void testCreateDishWithWrongType() {
        DishDTO newDishDTO = new DishDTO();
        newDishDTO.setName("CorrectName");
        newDishDTO.setDishType("invalidType");
        assertThrows(BadRequest.class, () ->  dishService.createDish(newDishDTO));
        verify(dishRepository, times(1)).getByName(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
        verify(dishRepository, never()).save(any());
    }

    @Test
    void testCreateDishOk() {
        DishDTO newDishDTO = new DishDTO();
        newDishDTO.setName("CorrectName");
        newDishDTO.setDishType("meat");
        dishService.createDish(newDishDTO);
        verify(dishRepository, times(1)).getByName(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
        verify(dishRepository, times(1)).save(any());
    }*/

    @Test
    void testGetNonExistentDish() {
        assertThrows(NotFound.class, () ->  dishService.getDish(1));
        verify(dishRepository, times(1)).existsById(any());
        verify(dishRepository, never()).getById(any());
    }

    @Test
    void testGetExistentDish() {
        Dish dish = new Dish();
        dish.setName("SomeName");
        dish.setDishType(DishType.SALAD);
        when(dishRepository.existsById(any(Long.class))).thenReturn(true);
        when(dishRepository.getById(any(Long.class))).thenReturn(dish);
        DishDTO dishDTO = dishService.getDish(1);
        verify(dishRepository, times(1)).existsById(any());
        verify(dishRepository, times(1)).getById(any());
        assertEquals(dish.getName(), dishDTO.getName());
        assertEquals(DishType.SALAD.toString().toLowerCase(Locale.ROOT), dishDTO.getDishType());
    }

    @Test
    void testUpdateNonExistentDish() {
        Dish dish = new Dish();
        DishDTO newDishDTO = new DishDTO();
        newDishDTO.setName("UpdatedName");
        newDishDTO.setDishType("meat");
        assertThrows(NotFound.class, () ->  dishService.updateDish(1, new String()));
        verify(dishRepository, times(1)).existsById(any());
        verify(dishRepository, never()).getById(any());
        verify(dishRepository, never()).getByName(any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(dishRepository, never()).save(any());
    }

    @Test
    void testUpdateDishWithAlreadyExistentDishName() {
        Dish dish = new Dish();
        DishDTO newDishDTO = new DishDTO();
        newDishDTO.setName("UpdatedName");
        newDishDTO.setDishType("meat");
        when(dishRepository.existsById(any(Long.class))).thenReturn(true);
        when(dishRepository.getById(any(Long.class))).thenReturn(dish);
        when(dishRepository.getByName(any(String.class))).thenReturn(dish);
        assertThrows(ConflictBetweenData.class, () ->  dishService.updateDish(1, new String()));
        verify(dishRepository, times(1)).existsById(any());
        verify(dishRepository, times(1)).getById(any());
        verify(dishRepository, times(1)).getByName(any());
        verify(validationService, never()).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(dishRepository, never()).save(any());
    }

    @Test
    void testUpdateDishWithNewNameHasIncorrectSymbols() {
        Dish dish = new Dish();
        DishDTO newDishDTO = new DishDTO();
        newDishDTO.setName("@!*%");
        newDishDTO.setDishType("meat");
        when(dishRepository.existsById(any(Long.class))).thenReturn(true);
        when(dishRepository.getById(any(Long.class))).thenReturn(dish);
        assertThrows(BadRequest.class, () ->  dishService.updateDish(1, new String()));
        verify(dishRepository, times(1)).existsById(any());
        verify(dishRepository, times(1)).getById(any());
        verify(dishRepository, times(1)).getByName(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, never()).isNameLengthValid(any());
        verify(dishRepository, never()).save(any());
    }

    @Test
    void testUpdateDishWithTooShortName() {
        Dish dish = new Dish();
        DishDTO newDishDTO = new DishDTO();
        newDishDTO.setName("d");
        newDishDTO.setDishType("UpdatedName");
        when(dishRepository.existsById(any(Long.class))).thenReturn(true);
        when(dishRepository.getById(any(Long.class))).thenReturn(dish);
        assertThrows(BadRequest.class, () ->  dishService.updateDish(1, new String()));
        verify(dishRepository, times(1)).existsById(any());
        verify(dishRepository, times(1)).getById(any());
        verify(dishRepository, times(1)).getByName(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
        verify(dishRepository, never()).save(any());
    }

    @Test
    void testUpdateDishWithWrongType() {
        DishDTO newDishDTO = new DishDTO();
        newDishDTO.setName("UpdatedName");
        newDishDTO.setDishType("invalidType");
        assertThrows(BadRequest.class, () ->  dishService.createDish(new String()));
        verify(dishRepository, times(1)).getByName(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
        verify(dishRepository, never()).save(any());
    }

    @Test
    void testUpdateDishOk() {
        Dish dish = new Dish();
        DishDTO newDishDTO = new DishDTO();
        newDishDTO.setName("UpdatedName");
        newDishDTO.setDishType("meat");
        when(dishRepository.existsById(any(Long.class))).thenReturn(true);
        when(dishRepository.getById(any(Long.class))).thenReturn(dish);
        dishService.updateDish(1, new String());
        verify(dishRepository, times(1)).existsById(any());
        verify(dishRepository, times(1)).getById(any());
        verify(dishRepository, times(1)).getByName(any());
        verify(validationService, times(1)).isNameCorrect(any());
        verify(validationService, times(1)).isNameLengthValid(any());
        verify(dishRepository, times(1)).save(any());
    }

    @Test
    void testDeleteNonExistentDish() {
        assertThrows(NotFound.class, () ->  dishService.deleteDish(1));
        verify(dishRepository, times(1)).existsById(any());
        verify(dishRepository, never()).getById(any());
    }

    @Test
    void testDeleteExistentDish() {
        Dish dish = new Dish();
        when(dishRepository.existsById(any(Long.class))).thenReturn(true);
        when(dishRepository.getById(any(Long.class))).thenReturn(dish);
        dishService.deleteDish(1);
        verify(dishRepository, times(1)).existsById(any());
        verify(dishRepository, times(1)).getById(any());
    }

}
