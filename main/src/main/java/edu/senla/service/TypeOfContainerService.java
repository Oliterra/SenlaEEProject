package edu.senla.service;

import edu.senla.dao.TypeOfContainerRepositoryInterface;
import edu.senla.dto.TypeOfContainerDTO;
import edu.senla.dto.TypeOfContainerForUpdateDTO;
import edu.senla.entity.TypeOfContainer;
import edu.senla.service.serviceinterface.TypeOfContainerServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class TypeOfContainerService implements TypeOfContainerServiceInterface {

    private final TypeOfContainerRepositoryInterface typeOfContainerRepository;

    private final ModelMapper mapper;

    public List<TypeOfContainerDTO> getAllTypesOfContainer() {
        Page<TypeOfContainer> typeOfContainers = typeOfContainerRepository.findAll(PageRequest.of(0, 10, Sort.by("numberOfCalories").descending()));
        return typeOfContainers.stream().map(t -> mapper.map(t, TypeOfContainerDTO.class)).toList();
    }

    public void createTypeOfContainer(TypeOfContainerDTO newTypeOfContainerDTO) {
        typeOfContainerRepository.save(mapper.map(newTypeOfContainerDTO, TypeOfContainer.class));
    }

    public TypeOfContainerDTO getTypeOfContainer(long id) {
        try {
            return mapper.map(typeOfContainerRepository.getById(id), TypeOfContainerDTO.class);
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public void updateTypeOfContainer(long id, TypeOfContainerForUpdateDTO updatedTypeOfContainerDTODTO) {
        TypeOfContainer updatedTypeOfContainer = mapper.map(updatedTypeOfContainerDTODTO, TypeOfContainer.class);
        TypeOfContainer typeOfContainerToUpdate = typeOfContainerRepository.getById(id);
        TypeOfContainer typeOfContainerWithNewParameters = updateTypeOfContainerOptions(typeOfContainerToUpdate, updatedTypeOfContainer);
        typeOfContainerRepository.save(typeOfContainerWithNewParameters);
    }

    public void deleteTypeOfContainer(long id) {
        typeOfContainerRepository.deleteById(id);
    }

    public boolean isTypeOfContainerExists(String name) {
        return typeOfContainerRepository.getByName(name) != null;
    }

    public boolean isTypeOfContainerExists(long caloricContent) {
        return typeOfContainerRepository.existsById(caloricContent);
    }

    public boolean isTypeOfContainerExists(String name, long caloricContent) {
        return isTypeOfContainerExists(name) && isTypeOfContainerExists(caloricContent);
    }

    public TypeOfContainer getTypeOfContainerByName(String name) {
        return typeOfContainerRepository.getByName(name);
    }

    private TypeOfContainer updateTypeOfContainerOptions(TypeOfContainer typeOfContainer, TypeOfContainer updatedTypeOfContainer) {
        typeOfContainer.setPrice(updatedTypeOfContainer.getPrice());
        typeOfContainer.setName(updatedTypeOfContainer.getName());
        return typeOfContainer;
    }

}
