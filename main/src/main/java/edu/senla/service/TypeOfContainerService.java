package edu.senla.service;

import edu.senla.dao.DAO;
import edu.senla.dto.TypeOfContainerDTO;
import edu.senla.entity.TypeOfContainer;
import edu.senla.service.serviceinterface.TypeOfContainerServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TypeOfContainerService implements TypeOfContainerServiceInterface {

    private final DAO<TypeOfContainer> typeOfContainerDAO;

    private final ModelMapper mapper;

    @Override
    public void createTypeOfContainer(TypeOfContainerDTO newTypeOfContainerDTO) {
        typeOfContainerDAO.create(mapper.map(newTypeOfContainerDTO, TypeOfContainer.class));
    }

    @Override
    public TypeOfContainerDTO read(int id) {
        TypeOfContainer requestedTypeOfContainer = typeOfContainerDAO.read(id);
        return mapper.map(requestedTypeOfContainer, TypeOfContainerDTO.class);
    }

    @Override
    public TypeOfContainer update(int id, TypeOfContainerDTO updatedTypeOfContainerDTO) {
        TypeOfContainer updatedTypeOfContainer = mapper.map(updatedTypeOfContainerDTO, TypeOfContainer.class);
        return updateTypeOfContainerOptions(typeOfContainerDAO.read(id), updatedTypeOfContainer);
    }

    @Override
    public void delete(int id) {
        typeOfContainerDAO.delete(id);
    }

    private TypeOfContainer updateTypeOfContainerOptions(TypeOfContainer typeOfContainer, TypeOfContainer updatedTypeOfContainer)
    {
        typeOfContainer.setNumberOfCalories(updatedTypeOfContainer.getNumberOfCalories());
        typeOfContainer.setPrice(updatedTypeOfContainer.getPrice());
        typeOfContainer.setName(updatedTypeOfContainer.getName());
        return typeOfContainer;
    }

}
