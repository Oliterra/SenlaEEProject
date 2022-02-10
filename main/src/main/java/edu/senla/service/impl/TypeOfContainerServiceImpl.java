package edu.senla.service.impl;

import edu.senla.dao.TypeOfContainerRepository;
import edu.senla.exeption.BadRequest;
import edu.senla.exeption.ConflictBetweenData;
import edu.senla.exeption.NotFound;
import edu.senla.model.dto.TypeOfContainerDTO;
import edu.senla.model.dto.TypeOfContainerForUpdateDTO;
import edu.senla.model.entity.TypeOfContainer;
import edu.senla.model.enums.CRUDOperations;
import edu.senla.service.TypeOfContainerService;
import edu.senla.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class TypeOfContainerServiceImpl implements TypeOfContainerService {

    private final TypeOfContainerRepository typeOfContainerRepository;
    private final ValidationService validationService;
    private final ModelMapper mapper;

    public List<TypeOfContainerDTO> getAllTypesOfContainer(int pages) {
        log.info("Getting all types of container");
        Page<TypeOfContainer> typeOfContainers = typeOfContainerRepository.findAll(PageRequest.of(0, pages, Sort.by("numberOfCalories").descending()));
        return typeOfContainers.stream().map(t -> mapper.map(t, TypeOfContainerDTO.class)).toList();
    }

    public void createTypeOfContainer(TypeOfContainerDTO newTypeOfContainerDTO) {
        log.info("A request to create a type of container {} was received", newTypeOfContainerDTO);
        isTypeOfContainerExists(newTypeOfContainerDTO.getName(), newTypeOfContainerDTO.getNumberOfCalories());
        checkTypeOfContainerName(newTypeOfContainerDTO.getName());
        checkTypeOfContainerNumberOfCalories(newTypeOfContainerDTO.getNumberOfCalories());
        TypeOfContainer typeOfContainer = mapper.map(newTypeOfContainerDTO, TypeOfContainer.class);
        typeOfContainerRepository.save(typeOfContainer);
        log.info("Type of container with name and number of calories successfully created");
    }

    public TypeOfContainerDTO getTypeOfContainer(long id) {
        log.info("Getting type of container with id {}: ", id);
        TypeOfContainer typeOfContainer = getTypeOfContainerIfExists(id, CRUDOperations.READ);
        TypeOfContainerDTO typeOfContainerDTO = mapper.map(typeOfContainer, TypeOfContainerDTO.class);
        log.info("Type of container found: {}: ", typeOfContainerDTO);
        return typeOfContainerDTO;
    }

    public void updateTypeOfContainer(long id, TypeOfContainerForUpdateDTO updatedTypeOfContainerDTODTO) {
        TypeOfContainer typeOfContainerToUpdate = getTypeOfContainerIfExists(id, CRUDOperations.UPDATE);
        log.info("Updating type of container with id {} with new data {}: ", id, updatedTypeOfContainerDTODTO);
        checkTypeOfContainerNameExistence(updatedTypeOfContainerDTODTO.getName());
        checkTypeOfContainerName(updatedTypeOfContainerDTODTO.getName());
        TypeOfContainer updatedTypeOfContainer = mapper.map(updatedTypeOfContainerDTODTO, TypeOfContainer.class);
        TypeOfContainer typeOfContainerWithNewParameters = updateTypeOfContainerOptions(typeOfContainerToUpdate, updatedTypeOfContainer);
        typeOfContainerRepository.save(typeOfContainerWithNewParameters);
        log.info("Type of container with id {} successfully updated", id);
    }

    public void deleteTypeOfContainer(long id) {
        log.info("Deleting type of container with id: {}", id);
        checkTypeOfContainerExistence(id);
        typeOfContainerRepository.deleteById(id);
        log.info("Type of container with id {} successfully deleted", id);
    }

    public boolean isTypeOfContainerExists(String name) {
        return typeOfContainerRepository.getByName(name) != null;
    }

    public TypeOfContainer getTypeOfContainerByName(String name) {
        return typeOfContainerRepository.getByName(name);
    }

    private boolean isTypeOfContainerExists(long caloricContent) {
        return typeOfContainerRepository.existsById(caloricContent);
    }

    private TypeOfContainer getTypeOfContainerIfExists(long id, CRUDOperations operation) {
        if (!typeOfContainerRepository.existsById(id)) {
            log.info("The attempt to {} a type of container failed, there is no type of container with id {}", operation.toString().toLowerCase(), id);
            throw new NotFound("There is no type of container with id " + id);
        }
        return typeOfContainerRepository.getById(id);
    }

    private void checkTypeOfContainerExistence(long id) {
        if (!typeOfContainerRepository.existsById(id)) {
            log.info("The attempt to delete a type of container failed, there is no type of container with id {}", id);
            throw new NotFound("There is no type of container with id " + id);
        }
    }

    private void checkTypeOfContainerNumberOfCalories(long typeOfContainerNumberOfCalories) {
        if (typeOfContainerNumberOfCalories < 0 || typeOfContainerNumberOfCalories > 5000) {
            log.error("The attempt to create a new type of container failed, a container number of calories {} invalid", typeOfContainerNumberOfCalories);
            throw new BadRequest("Type of container number of calories " + typeOfContainerNumberOfCalories + " is invalid");
        }
    }

    private void checkTypeOfContainerName(String typeOfContainerName) {
        if (!validationService.isTypeOContainerNameCorrect(typeOfContainerName)) {
            log.error("The attempt to create a new type of container failed, a type of container name {} invalid", typeOfContainerName);
            throw new BadRequest("Type of container name " + typeOfContainerName + " is invalid");
        }
    }

    private void isTypeOfContainerExists(String typeOfContainerName, long typeOfContainerNumberOfCalories) {
        if (isTypeOfContainerExists(typeOfContainerName)) {
            log.error("The attempt to create a new type of container failed, a type of container with name {} already exists", typeOfContainerName);
            throw new ConflictBetweenData("Type of container with name " + typeOfContainerName + " already exists");
        }
        if (isTypeOfContainerExists(typeOfContainerNumberOfCalories)) {
            log.error("The attempt to create a new type of container failed, a type of container with number of calories {} already exists", typeOfContainerNumberOfCalories);
            throw new ConflictBetweenData("Type of container with number of calories " + typeOfContainerNumberOfCalories + " already exists");
        }
    }

    private void checkTypeOfContainerNameExistence(String typeOfContainerName) {
        if (isTypeOfContainerExists(typeOfContainerName)) {
            log.error("The attempt to create a new type of container failed, a type of container with name {} already exists", typeOfContainerName);
            throw new ConflictBetweenData("Type of container with name " + typeOfContainerName + " already exists");
        }
    }

    private TypeOfContainer updateTypeOfContainerOptions(TypeOfContainer typeOfContainer, TypeOfContainer updatedTypeOfContainer) {
        typeOfContainer.setPrice(updatedTypeOfContainer.getPrice());
        typeOfContainer.setName(updatedTypeOfContainer.getName());
        return typeOfContainer;
    }
}
