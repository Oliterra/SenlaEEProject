package edu.senla.service;

import edu.senla.dao.TypeOfContainerRepositoryInterface;
import edu.senla.dto.TypeOfContainerDTO;
import edu.senla.entity.TypeOfContainer;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TypeOfContainerServiceTest {

    @Mock
    private TypeOfContainerRepositoryInterface typeOfContainerRepository;

    @Spy
    private ModelMapper mapper;

    @InjectMocks
    private TypeOfContainerService typeOfContainerService;

    private TypeOfContainer typeOfContainer;

    private int typeOfContainerNumberOfCalories;
    private int typeOfContainerPrice;
    private String typeOfContainerName;

    @BeforeEach
    void setup() {
        typeOfContainerNumberOfCalories = 1;
        typeOfContainerPrice = 30;
        typeOfContainerName = "TestName";

        typeOfContainer = new TypeOfContainer();

        typeOfContainer.setNumberOfCalories(typeOfContainerNumberOfCalories);
        typeOfContainer.setPrice(typeOfContainerPrice);
        typeOfContainer.setName(typeOfContainerName);
    }

    @Test
    void createTypeOfContainer() {
        when(typeOfContainerRepository.save(any(TypeOfContainer.class))).thenReturn(typeOfContainer);

        TypeOfContainerDTO typeOfContainerParamsDTO = new TypeOfContainerDTO(typeOfContainerNumberOfCalories, typeOfContainerName, typeOfContainerPrice);
        TypeOfContainerDTO createdTypeOfContainerDTO = typeOfContainerService.createTypeOfContainer(typeOfContainerParamsDTO);

        verify(typeOfContainerRepository, times(1)).save(any());

        Assert.assertEquals(typeOfContainerNumberOfCalories, createdTypeOfContainerDTO.getNumberOfCalories());
        Assert.assertEquals(typeOfContainerName, createdTypeOfContainerDTO.getName());
        Assert.assertEquals(typeOfContainerPrice, createdTypeOfContainerDTO.getPrice());
    }

    @Test
    void createNullTypeOfContainer() {
        TypeOfContainerDTO invalidTypeOfContainerDTO = null;
        Assert.assertThrows(IllegalArgumentException.class, () -> typeOfContainerService.createTypeOfContainer(invalidTypeOfContainerDTO));
    }

    @Test
    void readTypeOfContainer() {
        when(typeOfContainerRepository.getById(any(Long.class))).thenReturn(typeOfContainer);

        TypeOfContainerDTO readTypeOfContainerDTO = typeOfContainerService.readTypeOfContainer(typeOfContainerNumberOfCalories);

        verify(typeOfContainerRepository, times(1)).getById(any());

        Assert.assertEquals(typeOfContainerNumberOfCalories, readTypeOfContainerDTO.getNumberOfCalories());
        Assert.assertEquals(typeOfContainerName, readTypeOfContainerDTO.getName());
        Assert.assertEquals(typeOfContainerPrice, readTypeOfContainerDTO.getPrice());
    }

    @Test
    void readNullTypeOfContainer() {
        Assert.assertThrows(IllegalArgumentException.class, () -> typeOfContainerService.readTypeOfContainer(0));
    }

    @Test
    void updateTypeOfContainer() {
        int newPrice = 25;
        String newName = "Another name";

        TypeOfContainer updatedTypeOfContainer= new TypeOfContainer();
        updatedTypeOfContainer.setNumberOfCalories(typeOfContainerNumberOfCalories);
        updatedTypeOfContainer.setName(newName);
        updatedTypeOfContainer.setPrice(newPrice);

        when(typeOfContainerRepository.save(any(TypeOfContainer.class))).thenReturn(updatedTypeOfContainer);
        when(typeOfContainerRepository.getById(any(Long.class))).thenReturn(updatedTypeOfContainer);

        TypeOfContainerDTO updatedTypeOfContainerParamsDTO = new TypeOfContainerDTO(typeOfContainerNumberOfCalories, newName, newPrice);
        TypeOfContainerDTO updatedCTypeOfContainerDTO = typeOfContainerService.updateTypeOfContainer(typeOfContainerNumberOfCalories, updatedTypeOfContainerParamsDTO);

        verify(typeOfContainerRepository, times(1)).save(any());

        Assert.assertEquals(typeOfContainerNumberOfCalories, updatedCTypeOfContainerDTO.getNumberOfCalories());
        Assert.assertEquals(newName, updatedCTypeOfContainerDTO.getName());
        Assert.assertEquals(newPrice, updatedCTypeOfContainerDTO.getPrice());
    }

    @Test
    void updateNullTypeOfContainer() {
        int newPrice = 25;
        String newName = "Another name";
        TypeOfContainerDTO updatedTypeOfContainerParamsDTO = new TypeOfContainerDTO(typeOfContainerNumberOfCalories, newName, newPrice);

        TypeOfContainerDTO invalidTypeOfContainerDTO = null;
        Assert.assertThrows(NullPointerException.class, () -> typeOfContainerService.updateTypeOfContainer(invalidTypeOfContainerDTO.getNumberOfCalories(), updatedTypeOfContainerParamsDTO));
    }

    @Test
    void updateTypeOfContainerWithNullParams() {
        TypeOfContainerDTO invalidUpdatedTypeOfContainerParamsDTO = null;

        Assert.assertThrows(IllegalArgumentException.class, () -> typeOfContainerService.updateTypeOfContainer(typeOfContainerNumberOfCalories, invalidUpdatedTypeOfContainerParamsDTO));
    }

    @Test
    void deleteTypeOfContainer() {
        typeOfContainerService.deleteTypeOfContainer(typeOfContainerNumberOfCalories);
        verify(typeOfContainerRepository, times(1)).delete(any());
    }

    @Test
    void deleteNullTypeOfContainer() {
        TypeOfContainerDTO invalidTypeOfContainerDTO = null;
        Assert.assertThrows(NullPointerException.class, () -> typeOfContainerService.deleteTypeOfContainer(invalidTypeOfContainerDTO.getNumberOfCalories()));
    }

    @Test
    void isExistentTypeOfContainerExists() {
        when(typeOfContainerRepository.getTypeOfContainerByCaloricContent(any(Integer.class))).thenReturn(typeOfContainer);

        TypeOfContainerDTO typeOfContainer = new TypeOfContainerDTO();
        typeOfContainer.setNumberOfCalories(typeOfContainerNumberOfCalories);

        boolean typeOfContainerExists = typeOfContainerService.isTypeOfContainerExists(typeOfContainer);

        verify(typeOfContainerRepository, times(1)).getTypeOfContainerByCaloricContent(any(Integer.class));

        Assert.assertTrue(typeOfContainerExists);
    }

    @Test
    void isNonExistentTypeOfContainerExists() {
        when(typeOfContainerRepository.getTypeOfContainerByCaloricContent(any(Integer.class))).thenThrow(new NoResultException());

        TypeOfContainerDTO typeOfContainer = new TypeOfContainerDTO();
        typeOfContainer.setNumberOfCalories(typeOfContainerNumberOfCalories);

        boolean typeOfContainerExists = typeOfContainerService.isTypeOfContainerExists(typeOfContainer);

        verify(typeOfContainerRepository, times(1)).getTypeOfContainerByCaloricContent(any(Integer.class));

        Assert.assertThrows(NoResultException.class, () -> typeOfContainerRepository.getTypeOfContainerByCaloricContent(typeOfContainerNumberOfCalories));
        Assert.assertFalse(typeOfContainerExists);
    }

    @Test
    void isNullTypeOfContainerExists() {
        TypeOfContainerDTO invalidTypeOfContainerDTO = null;

        Assert.assertThrows(NullPointerException.class, () -> typeOfContainerService.isTypeOfContainerExists(invalidTypeOfContainerDTO));
    }

}
