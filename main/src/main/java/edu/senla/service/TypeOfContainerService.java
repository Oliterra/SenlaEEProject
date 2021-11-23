package edu.senla.service;

import edu.senla.dao.daointerface.TypeOfContainerRepositoryInterface;
import edu.senla.dto.TypeOfContainerDTO;
import edu.senla.entity.TypeOfContainer;
import edu.senla.service.serviceinterface.TypeOfContainerServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class TypeOfContainerService implements TypeOfContainerServiceInterface {

    private final TypeOfContainerRepositoryInterface typeOfContainerRepository;

    private final ModelMapper mapper;

    @Override
    public void createTypeOfContainer(TypeOfContainerDTO newTypeOfContainerDTO) {
        typeOfContainerRepository.create(mapper.map(newTypeOfContainerDTO, TypeOfContainer.class));
    }

    @Override
    public TypeOfContainerDTO readTypeOfContainer(int id) {
        TypeOfContainer requestedTypeOfContainer = typeOfContainerRepository.read(id);
        return mapper.map(requestedTypeOfContainer, TypeOfContainerDTO.class);
    }

    @Override
    public void updateTypeOfContainer(int id, TypeOfContainerDTO updatedTypeOfContainerDTO) {
        TypeOfContainer updatedTypeOfContainer = mapper.map(updatedTypeOfContainerDTO, TypeOfContainer.class);
        TypeOfContainer typeOfContainerToUpdate = mapper.map(readTypeOfContainer(id), TypeOfContainer.class);
        TypeOfContainer typeOfContainerWithNewParameters = updateTypeOfContainerOptions(typeOfContainerToUpdate, updatedTypeOfContainer);
        typeOfContainerRepository.update(typeOfContainerWithNewParameters);
    }

    @Override
    public void deleteTypeOfContainer(int id) {
        typeOfContainerRepository.delete(id);
    }

    private TypeOfContainer updateTypeOfContainerOptions(TypeOfContainer typeOfContainer, TypeOfContainer updatedTypeOfContainer)
    {
        typeOfContainer.setNumberOfCalories(updatedTypeOfContainer.getNumberOfCalories());
        typeOfContainer.setPrice(updatedTypeOfContainer.getPrice());
        typeOfContainer.setName(updatedTypeOfContainer.getName());
        return typeOfContainer;
    }

}
