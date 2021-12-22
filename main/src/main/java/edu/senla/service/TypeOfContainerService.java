package edu.senla.service;

import edu.senla.dao.TypeOfContainerRepositoryInterface;
import edu.senla.dto.TypeOfContainerDTO;
import edu.senla.entity.TypeOfContainer;
import edu.senla.service.serviceinterface.TypeOfContainerServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class TypeOfContainerService implements TypeOfContainerServiceInterface {

    private final TypeOfContainerRepositoryInterface typeOfContainerRepository;

    private final ModelMapper mapper;

    @Override
    public TypeOfContainerDTO createTypeOfContainer(TypeOfContainerDTO newTypeOfContainerDTO) {
        TypeOfContainer newTypeOfContainer = typeOfContainerRepository.save(mapper.map(newTypeOfContainerDTO, TypeOfContainer.class));
        return mapper.map(newTypeOfContainer, TypeOfContainerDTO.class);
    }

    @Override
    public TypeOfContainerDTO readTypeOfContainer(long id) {
        TypeOfContainer requestedTypeOfContainer = typeOfContainerRepository.getById(id);
        return mapper.map(requestedTypeOfContainer, TypeOfContainerDTO.class);
    }

    @Override
    public TypeOfContainerDTO updateTypeOfContainer(long id, TypeOfContainerDTO updatedTypeOfContainerDTO) {
        TypeOfContainer updatedTypeOfContainer = mapper.map(updatedTypeOfContainerDTO, TypeOfContainer.class);
        TypeOfContainer typeOfContainerToUpdate = typeOfContainerRepository.getById(id);

        TypeOfContainer typeOfContainerWithNewParameters = updateTypeOfContainerOptions(typeOfContainerToUpdate, updatedTypeOfContainer);
        TypeOfContainer typeOfContainer = typeOfContainerRepository.save(typeOfContainerWithNewParameters);

        return mapper.map(typeOfContainer, TypeOfContainerDTO.class);
    }

    @Override
    public void deleteTypeOfContainer(long id) {
        typeOfContainerRepository.deleteById(id);
    }

    private TypeOfContainer updateTypeOfContainerOptions(TypeOfContainer typeOfContainer, TypeOfContainer updatedTypeOfContainer)
    {
        typeOfContainer.setNumberOfCalories(updatedTypeOfContainer.getNumberOfCalories());
        typeOfContainer.setPrice(updatedTypeOfContainer.getPrice());
        typeOfContainer.setName(updatedTypeOfContainer.getName());
        return typeOfContainer;
    }

    @Override
    public boolean isTypeOfContainerExists(TypeOfContainerDTO typeOfContainer) {
        try {
            return typeOfContainerRepository.getTypeOfContainerByCaloricContent(typeOfContainer.getNumberOfCalories()) != null;
        }
        catch (NoResultException e){
            return false;
        }
    }

}
