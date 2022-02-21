package edu.senla.service;

import edu.senla.dao.TypeOfContainerRepository;
import edu.senla.model.dto.ContainerTypeDTO;
import edu.senla.model.dto.ContainerTypeForUpdateDTO;
import edu.senla.model.entity.ContainerType;
import edu.senla.exeption.BadRequest;
import edu.senla.exeption.ConflictBetweenData;
import edu.senla.exeption.NotFound;
import edu.senla.service.impl.ContainerTypeServiceImpl;
import edu.senla.service.impl.ValidationServiceImpl;
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
class ContainerTypeServiceTest {

    @Mock
    private TypeOfContainerRepository typeOfContainerRepository;

    @Spy
    private ModelMapper mapper;

    @Spy
    private ValidationServiceImpl validationService;

    @InjectMocks
    private ContainerTypeServiceImpl typeOfContainerService;

    @Test
    void testCreteTypeOfContainerWithAlreadyExistentName() {
        ContainerTypeDTO containerTypeDTO = new ContainerTypeDTO();
        containerTypeDTO.setName("XL");
        containerTypeDTO.setNumberOfCalories(1200);
        when(typeOfContainerRepository.getByName(any(String.class))).thenReturn(new ContainerType());
        assertThrows(ConflictBetweenData.class, () ->  typeOfContainerService.createTypeOfContainer(new String()));
        verify(typeOfContainerRepository, times(1)).getByName(any());
        verify(typeOfContainerRepository, never()).existsById(any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, never()).save(any());
    }

    @Test
    void testCreteTypeOfContainerWithAlreadyExistentNumberOfCalories() {
        ContainerTypeDTO containerTypeDTO = new ContainerTypeDTO();
        containerTypeDTO.setName("XL");
        containerTypeDTO.setNumberOfCalories(1200);
        when(typeOfContainerRepository.existsById(any(Long.class))).thenReturn(true);
        assertThrows(ConflictBetweenData.class, () ->  typeOfContainerService.createTypeOfContainer(new String()));
        verify(typeOfContainerRepository, times(1)).getByName(any());
        verify(typeOfContainerRepository, times(1)).existsById(any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, never()).save(any());
    }

    @Test
    void testCreteTypeOfContainerWithInvalidName() {
        ContainerTypeDTO containerTypeDTO = new ContainerTypeDTO();
        containerTypeDTO.setName("wrong");
        containerTypeDTO.setNumberOfCalories(1200);
        assertThrows(BadRequest.class, () ->  typeOfContainerService.createTypeOfContainer(new String()));
        verify(typeOfContainerRepository, times(1)).getByName(any());
        verify(typeOfContainerRepository, times(1)).existsById(any());
        verify(validationService, times(1)).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, never()).save(any());
    }

    @Test
    void testCreteTypeOfContainerWithInvalidNumberOfCalories() {
        ContainerTypeDTO containerTypeDTO = new ContainerTypeDTO();
        containerTypeDTO.setName("XL");
        containerTypeDTO.setNumberOfCalories(50000);
        assertThrows(BadRequest.class, () ->  typeOfContainerService.createTypeOfContainer(new String()));
        verify(typeOfContainerRepository, times(1)).getByName(any());
        verify(typeOfContainerRepository, times(1)).existsById(any());
        verify(validationService, times(1)).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, never()).save(any());
    }

    @Test
    void testCreteTypeOfContainerOk() {
        ContainerTypeDTO containerTypeDTO = new ContainerTypeDTO();
        containerTypeDTO.setName("XL");
        containerTypeDTO.setNumberOfCalories(1200);
        typeOfContainerService.createTypeOfContainer(new String());
        verify(typeOfContainerRepository, times(1)).getByName(any());
        verify(typeOfContainerRepository, times(1)).existsById(any());
        verify(validationService, times(1)).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, times(1)).save(any());
    }

    @Test
    void testUpdateNonExistentTypeOfContainer() {
        ContainerTypeForUpdateDTO containerTypeForUpdateDTO = new ContainerTypeForUpdateDTO();
        containerTypeForUpdateDTO.setName("L");
        containerTypeForUpdateDTO.setPrice(16);
        assertThrows(NotFound.class, () -> typeOfContainerService.updateTypeOfContainer(1, new String()));
        verify(typeOfContainerRepository, times(1)).existsById(any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, never()).save(any());
    }

    @Test
    void testUpdateTypeOfContainerWithAlreadyExistentName() {
        ContainerTypeForUpdateDTO containerTypeForUpdateDTO = new ContainerTypeForUpdateDTO();
        containerTypeForUpdateDTO.setName("L");
        containerTypeForUpdateDTO.setPrice(16);
        when(typeOfContainerRepository.existsById(any(Long.class))).thenReturn(true);
        when(typeOfContainerRepository.getById(any(Long.class))).thenReturn(new ContainerType());
        when(typeOfContainerRepository.getByName(any(String.class))).thenReturn(new ContainerType());
        assertThrows(ConflictBetweenData.class, () -> typeOfContainerService.updateTypeOfContainer(1, new String()));
        verify(typeOfContainerRepository, times(1)).existsById(any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, never()).save(any());
    }

    @Test
    void testUpdateTypeOfContainerWithInvalidName() {
        ContainerTypeForUpdateDTO containerTypeForUpdateDTO = new ContainerTypeForUpdateDTO();
        containerTypeForUpdateDTO.setName("wrong");
        containerTypeForUpdateDTO.setPrice(16);
        when(typeOfContainerRepository.existsById(any(Long.class))).thenReturn(true);
        when(typeOfContainerRepository.getById(any(Long.class))).thenReturn(new ContainerType());
        assertThrows(BadRequest.class, () -> typeOfContainerService.updateTypeOfContainer(1, new String()));
        verify(typeOfContainerRepository, times(1)).existsById(any());
        verify(validationService, times(1)).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, never()).save(any());
    }

    @Test
    void testUpdateTypeOfContainerOk() {
        ContainerTypeForUpdateDTO containerTypeForUpdateDTO = new ContainerTypeForUpdateDTO();
        containerTypeForUpdateDTO.setName("L");
        containerTypeForUpdateDTO.setPrice(16);
        when(typeOfContainerRepository.existsById(any(Long.class))).thenReturn(true);
        when(typeOfContainerRepository.getById(any(Long.class))).thenReturn(new ContainerType());
        typeOfContainerService.updateTypeOfContainer(1, new String());
        verify(typeOfContainerRepository, times(1)).existsById(any());
        verify(validationService, times(1)).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, times(1)).save(any());
    }

}
