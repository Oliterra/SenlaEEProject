package edu.senla.service;

import edu.senla.dao.TypeOfContainerRepositoryInterface;
import edu.senla.entity.TypeOfContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

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

}
