package edu.senla.service;

import edu.senla.dao.DishInformationRepository;
import edu.senla.dao.DishRepository;
import edu.senla.model.dto.DishInformationDTO;
import edu.senla.model.dto.DishInformationForUpdateDTO;
import edu.senla.model.entity.Dish;
import edu.senla.model.entity.DishInformation;
import edu.senla.exeption.ConflictBetweenData;
import edu.senla.exeption.NotFound;
import edu.senla.service.impl.DishInformationServiceImpl;
import edu.senla.service.impl.DishServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DishInformationServiceTest {

    @Mock
    private DishServiceImpl dishService;

    @Mock
    private DishInformationRepository dishInformationRepository;

    @Mock
    private DishRepository dishRepository;

    @Spy
    private ModelMapper mapper;

    @InjectMocks
    private DishInformationServiceImpl dishInformationService;

    @Test
    void testCreateDishInformationForNotExistentDish() {
        DishInformationDTO dishInformationDTO = new DishInformationDTO();
        dishInformationDTO.setDishId(1);
        dishInformationDTO.setDescription("some description");
        assertThrows(NotFound.class, () ->  dishInformationService.createDishInformation(new String()));
        verify(dishRepository, times(1)).existsById(any());
        verify(dishRepository, never()).getById(any());
        verify(dishRepository, never()).save(any());
    }

    @Test
    void testCreateDishInformationForDishThatAlreadyHasDishInformation() {
        DishInformationDTO dishInformationDTO = new DishInformationDTO();
        dishInformationDTO.setDishId(1);
        dishInformationDTO.setDescription("some description");
        when(dishRepository.existsById(any(Long.class))).thenReturn(true);
        when(dishService.isDishHasDishInformation(any(Long.class))).thenReturn(true);
        assertThrows(ConflictBetweenData.class, () ->  dishInformationService.createDishInformation(new String()));
        verify(dishRepository, times(1)).existsById(any());
        verify(dishRepository, never()).getById(any());
        verify(dishRepository, never()).save(any());
    }

    @Test
    void testCreateDishInformationOk() {
        DishInformationDTO dishInformationDTO = new DishInformationDTO();
        dishInformationDTO.setDishId(1);
        dishInformationDTO.setDescription("some description");
        when(dishRepository.existsById(any(Long.class))).thenReturn(true);
        when(dishRepository.getById(any(Long.class))).thenReturn(new Dish());
        dishInformationService.createDishInformation(new String());
        verify(dishRepository, times(1)).existsById(any());
        verify(dishRepository, times(1)).getById(any());
        verify(dishRepository, times(1)).save(any());
    }

    @Test
    void testUpdateNonExistentDishInformation() {
        DishInformationForUpdateDTO dishInformationForUpdateDTO = new DishInformationForUpdateDTO();
        dishInformationForUpdateDTO.setDescription("some description");
        assertThrows(NotFound.class, () -> dishInformationService.updateDishInformation(1, new String()));
        verify(dishInformationRepository, times(1)).existsById(any());
        verify(dishInformationRepository, never()).getById(any());
        verify(dishInformationRepository, never()).save(any());
    }

    @Test
    void testUpdateDishInformationOk() {
        DishInformationForUpdateDTO dishInformationForUpdateDTO = new DishInformationForUpdateDTO();
        dishInformationForUpdateDTO.setDescription("some description");
        when(dishInformationRepository.existsById(any(Long.class))).thenReturn(true);
        when(dishInformationRepository.getById(any(Long.class))).thenReturn(new DishInformation());
        dishInformationService.updateDishInformation(1, new String());
        verify(dishInformationRepository, times(1)).existsById(any());
        verify(dishInformationRepository, times(1)).getById(any());
        verify(dishInformationRepository, times(1)).save(any());
    }

}
