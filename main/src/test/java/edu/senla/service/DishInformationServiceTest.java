package edu.senla.service;

import edu.senla.dao.DishInformationRepositoryInterface;
import edu.senla.dao.DishRepositoryInterface;
import edu.senla.dto.DishInformationDTO;
import edu.senla.dto.DishInformationForUpdateDTO;
import edu.senla.entity.Dish;
import edu.senla.entity.DishInformation;
import edu.senla.exeptions.ConflictBetweenData;
import edu.senla.exeptions.NotFound;
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
    private DishService dishService;

    @Mock
    private DishInformationRepositoryInterface dishInformationRepository;

    @Mock
    private DishRepositoryInterface dishRepository;

    @Spy
    private ModelMapper mapper;

    @InjectMocks
    private DishInformationService dishInformationService;

    @Test
    void testCreateDishInformationForNotExistentDish() {
        DishInformationDTO dishInformationDTO = new DishInformationDTO();
        dishInformationDTO.setDishId(1);
        dishInformationDTO.setDescription("some description");
        assertThrows(NotFound.class, () ->  dishInformationService.createDishInformation(dishInformationDTO));
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
        assertThrows(ConflictBetweenData.class, () ->  dishInformationService.createDishInformation(dishInformationDTO));
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
        dishInformationService.createDishInformation(dishInformationDTO);
        verify(dishRepository, times(1)).existsById(any());
        verify(dishRepository, times(1)).getById(any());
        verify(dishRepository, times(1)).save(any());
    }

    @Test
    void testUpdateNonExistentDishInformation() {
        DishInformationForUpdateDTO dishInformationForUpdateDTO = new DishInformationForUpdateDTO();
        dishInformationForUpdateDTO.setDescription("some description");
        assertThrows(NotFound.class, () -> dishInformationService.updateDishInformation(1, dishInformationForUpdateDTO));
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
        dishInformationService.updateDishInformation(1, dishInformationForUpdateDTO);
        verify(dishInformationRepository, times(1)).existsById(any());
        verify(dishInformationRepository, times(1)).getById(any());
        verify(dishInformationRepository, times(1)).save(any());
    }

}
