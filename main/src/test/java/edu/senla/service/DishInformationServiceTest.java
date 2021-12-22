package edu.senla.service;

import edu.senla.dao.DishInformationRepositoryInterface;
import edu.senla.dao.DishRepositoryInterface;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DishInformationServiceTest {

    @Mock
    private DishInformationRepositoryInterface dishInformationRepositoryInterface;

    @Mock
    private DishRepositoryInterface dishRepository;

    @Spy
    private ModelMapper mapper;

    @InjectMocks
    private DishInformationService dishInformationService;

    private DishInformation dishInformation;
    private Dish dish;

    private int dishInformationId;
    private int dishInformationCaloricContent;
    private String dishInformationDescription;

    @BeforeEach
    void setup() {
        dishInformationId = 1;
        dishInformationCaloricContent = 300;
        dishInformationDescription = "200 g";

        dishInformation = new DishInformation();

        dishInformation.setId(dishInformationId);
        dishInformation.setCaloricContent(300);
        dishInformation.setDescription(dishInformationDescription);

        dish = new Dish();
        dish.setId(1);
    }

    @Test
    void createDishInformation() {
        when(dishInformationRepositoryInterface.save(any(DishInformation.class))).thenReturn(dishInformation);
        when(dishRepository.getById(any(Long.class))).thenReturn(dish);

        DishInformationDTO dishInformationParamsDTO = new DishInformationDTO(dishInformationId, dishInformationDescription, dishInformationCaloricContent);
        DishInformationDTO createdDishInformationDTO = dishInformationService.createDishInformation(1, dishInformationParamsDTO);

        verify(dishInformationRepositoryInterface, times(1)).save(any());

        Assert.assertEquals(dishInformationId, createdDishInformationDTO.getId());
        Assert.assertEquals(dishInformationCaloricContent, createdDishInformationDTO.getCaloricContent());
        Assert.assertEquals(dishInformationDescription, createdDishInformationDTO.getDescription());
    }

    @Test
    void createNullDishInformation() {
        DishInformationDTO invalidDishInformationDTO = null;
        Assert.assertThrows(IllegalArgumentException.class, () -> dishInformationService.createDishInformation(1, invalidDishInformationDTO));
    }

    @Test
    void createDishInformationForNullDish() {
        when(dishInformationRepositoryInterface.save(any(DishInformation.class))).thenReturn(dishInformation);
        when(dishRepository.getById(any(Long.class))).thenReturn(null);

        DishInformationDTO dishInformationParamsDTO = new DishInformationDTO(dishInformationId, dishInformationDescription, dishInformationCaloricContent);
        Assert.assertThrows(NullPointerException.class, () -> dishInformationService.createDishInformation(1, dishInformationParamsDTO));
    }

    @Test
    void readDishInformation() {
        when(dishInformationRepositoryInterface.getById(any(Long.class))).thenReturn(dishInformation);

        DishInformationDTO readDishInformationDTO = dishInformationService.readDishInformation(dishInformationId);

        verify(dishInformationRepositoryInterface, times(1)).getById(any());

        Assert.assertEquals(dishInformationId, readDishInformationDTO.getId());
        Assert.assertEquals(dishInformationCaloricContent, readDishInformationDTO.getCaloricContent());
        Assert.assertEquals(dishInformationDescription, readDishInformationDTO.getDescription());
    }

    @Test
    void readNullDishInformation() {
        Assert.assertThrows(IllegalArgumentException.class, () -> dishInformationService.readDishInformation(0));
    }

    @Test
    void updateDishInformation() {
        int newCaloricContent = 400;
        String newDescription = "500 g";

        DishInformation updatedDishInformation = new DishInformation();
        updatedDishInformation.setId(dishInformationId);
        updatedDishInformation.setCaloricContent(newCaloricContent);
        updatedDishInformation.setDescription(newDescription);

        when(dishInformationRepositoryInterface.save(any(DishInformation.class))).thenReturn(updatedDishInformation);
        when(dishInformationRepositoryInterface.getById(any(Long.class))).thenReturn(dishInformation);

        DishInformationDTO updatedDishInformationParamsDTO = new DishInformationDTO(dishInformationId, newDescription, newCaloricContent);
        DishInformationDTO updatedDishInformationDTO = dishInformationService.updateDishInformation(dishInformationId, updatedDishInformationParamsDTO);

        verify(dishInformationRepositoryInterface, times(1)).save(any());

        Assert.assertEquals(dishInformationId, updatedDishInformationDTO.getId());
        Assert.assertEquals(newCaloricContent, updatedDishInformationDTO.getCaloricContent());
        Assert.assertEquals(newDescription, updatedDishInformationDTO.getDescription());
    }

    @Test
    void updateNullDishInformation() {
        int newCaloricContent = 400;
        String newDescription = "500 g";
        DishInformationDTO updatedDishInformationParamsDTO = new DishInformationDTO(dishInformationId, newDescription, newCaloricContent);

        DishInformationDTO invalidDishInformationDTO = null;
        Assert.assertThrows(NullPointerException.class, () -> dishInformationService.updateDishInformation(invalidDishInformationDTO.getId(), updatedDishInformationParamsDTO));
    }

    @Test
    void updateDishInformationWithNullParams() {
        DishInformationDTO invalidDishInformationDTO = null;

        Assert.assertThrows(IllegalArgumentException.class, () -> dishInformationService.updateDishInformation(dishInformationId, invalidDishInformationDTO));
    }

    @Test
    void deleteDishInformation() {
        dishInformationService.deleteDishInformation(dishInformationId);
        verify(dishInformationRepositoryInterface, times(1)).delete(any());
    }

    @Test
    void deleteNullDishInformation() {
        DishInformationDTO invalidDishInformationDTO = null;
        Assert.assertThrows(NullPointerException.class, () -> dishInformationService.deleteDishInformation(invalidDishInformationDTO.getId()));
    }

}
