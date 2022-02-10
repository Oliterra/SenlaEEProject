package edu.senla.service;

import edu.senla.dao.TypeOfContainerRepository;
import edu.senla.model.dto.TypeOfContainerDTO;
import edu.senla.model.dto.TypeOfContainerForUpdateDTO;
import edu.senla.model.entity.TypeOfContainer;
import edu.senla.exeption.BadRequest;
import edu.senla.exeption.ConflictBetweenData;
import edu.senla.exeption.NotFound;
import edu.senla.service.impl.TypeOfContainerServiceImpl;
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
class TypeOfContainerServiceTest {

    @Mock
    private TypeOfContainerRepository typeOfContainerRepository;

    @Spy
    private ModelMapper mapper;

    @Spy
    private ValidationServiceImpl validationService;

    @InjectMocks
    private TypeOfContainerServiceImpl typeOfContainerService;

    @Test
    void testCreteTypeOfContainerWithAlreadyExistentName() {
        TypeOfContainerDTO typeOfContainerDTO = new TypeOfContainerDTO();
        typeOfContainerDTO.setName("XL");
        typeOfContainerDTO.setNumberOfCalories(1200);
        when(typeOfContainerRepository.getByName(any(String.class))).thenReturn(new TypeOfContainer());
        assertThrows(ConflictBetweenData.class, () ->  typeOfContainerService.createTypeOfContainer(typeOfContainerDTO));
        verify(typeOfContainerRepository, times(1)).getByName(any());
        verify(typeOfContainerRepository, never()).existsById(any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, never()).save(any());
    }

    @Test
    void testCreteTypeOfContainerWithAlreadyExistentNumberOfCalories() {
        TypeOfContainerDTO typeOfContainerDTO = new TypeOfContainerDTO();
        typeOfContainerDTO.setName("XL");
        typeOfContainerDTO.setNumberOfCalories(1200);
        when(typeOfContainerRepository.existsById(any(Long.class))).thenReturn(true);
        assertThrows(ConflictBetweenData.class, () ->  typeOfContainerService.createTypeOfContainer(typeOfContainerDTO));
        verify(typeOfContainerRepository, times(1)).getByName(any());
        verify(typeOfContainerRepository, times(1)).existsById(any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, never()).save(any());
    }

    @Test
    void testCreteTypeOfContainerWithInvalidName() {
        TypeOfContainerDTO typeOfContainerDTO = new TypeOfContainerDTO();
        typeOfContainerDTO.setName("wrong");
        typeOfContainerDTO.setNumberOfCalories(1200);
        assertThrows(BadRequest.class, () ->  typeOfContainerService.createTypeOfContainer(typeOfContainerDTO));
        verify(typeOfContainerRepository, times(1)).getByName(any());
        verify(typeOfContainerRepository, times(1)).existsById(any());
        verify(validationService, times(1)).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, never()).save(any());
    }

    @Test
    void testCreteTypeOfContainerWithInvalidNumberOfCalories() {
        TypeOfContainerDTO typeOfContainerDTO = new TypeOfContainerDTO();
        typeOfContainerDTO.setName("XL");
        typeOfContainerDTO.setNumberOfCalories(50000);
        assertThrows(BadRequest.class, () ->  typeOfContainerService.createTypeOfContainer(typeOfContainerDTO));
        verify(typeOfContainerRepository, times(1)).getByName(any());
        verify(typeOfContainerRepository, times(1)).existsById(any());
        verify(validationService, times(1)).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, never()).save(any());
    }

    @Test
    void testCreteTypeOfContainerOk() {
        TypeOfContainerDTO typeOfContainerDTO = new TypeOfContainerDTO();
        typeOfContainerDTO.setName("XL");
        typeOfContainerDTO.setNumberOfCalories(1200);
        typeOfContainerService.createTypeOfContainer(typeOfContainerDTO);
        verify(typeOfContainerRepository, times(1)).getByName(any());
        verify(typeOfContainerRepository, times(1)).existsById(any());
        verify(validationService, times(1)).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, times(1)).save(any());
    }

    @Test
    void testUpdateNonExistentTypeOfContainer() {
        TypeOfContainerForUpdateDTO typeOfContainerForUpdateDTO = new TypeOfContainerForUpdateDTO();
        typeOfContainerForUpdateDTO.setName("L");
        typeOfContainerForUpdateDTO.setPrice(16);
        assertThrows(NotFound.class, () -> typeOfContainerService.updateTypeOfContainer(1, typeOfContainerForUpdateDTO));
        verify(typeOfContainerRepository, times(1)).existsById(any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, never()).save(any());
    }

    @Test
    void testUpdateTypeOfContainerWithAlreadyExistentName() {
        TypeOfContainerForUpdateDTO typeOfContainerForUpdateDTO = new TypeOfContainerForUpdateDTO();
        typeOfContainerForUpdateDTO.setName("L");
        typeOfContainerForUpdateDTO.setPrice(16);
        when(typeOfContainerRepository.existsById(any(Long.class))).thenReturn(true);
        when(typeOfContainerRepository.getById(any(Long.class))).thenReturn(new TypeOfContainer());
        when(typeOfContainerRepository.getByName(any(String.class))).thenReturn(new TypeOfContainer());
        assertThrows(ConflictBetweenData.class, () -> typeOfContainerService.updateTypeOfContainer(1, typeOfContainerForUpdateDTO));
        verify(typeOfContainerRepository, times(1)).existsById(any());
        verify(validationService, never()).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, never()).save(any());
    }

    @Test
    void testUpdateTypeOfContainerWithInvalidName() {
        TypeOfContainerForUpdateDTO typeOfContainerForUpdateDTO = new TypeOfContainerForUpdateDTO();
        typeOfContainerForUpdateDTO.setName("wrong");
        typeOfContainerForUpdateDTO.setPrice(16);
        when(typeOfContainerRepository.existsById(any(Long.class))).thenReturn(true);
        when(typeOfContainerRepository.getById(any(Long.class))).thenReturn(new TypeOfContainer());
        assertThrows(BadRequest.class, () -> typeOfContainerService.updateTypeOfContainer(1, typeOfContainerForUpdateDTO));
        verify(typeOfContainerRepository, times(1)).existsById(any());
        verify(validationService, times(1)).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, never()).save(any());
    }

    @Test
    void testUpdateTypeOfContainerOk() {
        TypeOfContainerForUpdateDTO typeOfContainerForUpdateDTO = new TypeOfContainerForUpdateDTO();
        typeOfContainerForUpdateDTO.setName("L");
        typeOfContainerForUpdateDTO.setPrice(16);
        when(typeOfContainerRepository.existsById(any(Long.class))).thenReturn(true);
        when(typeOfContainerRepository.getById(any(Long.class))).thenReturn(new TypeOfContainer());
        typeOfContainerService.updateTypeOfContainer(1, typeOfContainerForUpdateDTO);
        verify(typeOfContainerRepository, times(1)).existsById(any());
        verify(validationService, times(1)).isTypeOContainerNameCorrect(any());
        verify(typeOfContainerRepository, times(1)).save(any());
    }

}
