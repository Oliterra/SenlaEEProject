package edu.senla.service.impl;

import edu.senla.dao.TypeOfContainerRepository;
import edu.senla.exeption.BadRequest;
import edu.senla.exeption.ConflictBetweenData;
import edu.senla.exeption.NotFound;
import edu.senla.model.dto.ContainerTypeDTO;
import edu.senla.model.dto.ContainerTypeForUpdateDTO;
import edu.senla.model.entity.ContainerType;
import edu.senla.model.enums.CRUDOperations;
import edu.senla.service.ContainerTypeService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
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
public class ContainerTypeServiceImpl extends AbstractService implements ContainerTypeService {

    private final TypeOfContainerRepository typeOfContainerRepository;

    public List<ContainerTypeDTO> getAllTypesOfContainer(int pages) {
        log.info("Getting all types of container");
        Page<ContainerType> typeOfContainers = typeOfContainerRepository.findAll(PageRequest.of(0, pages, Sort.by("numberOfCalories").descending()));
        return typeOfContainers.stream().map(t -> modelMapper.map(t, ContainerTypeDTO.class)).toList();
    }

    @SneakyThrows
    public void createTypeOfContainer(String typeOfContainerJson) {
        ContainerTypeDTO newContainerTypeDTO = objectMapper.readValue(typeOfContainerJson, ContainerTypeDTO.class);
        log.info("A request to create a type of container {} was received", newContainerTypeDTO);
        isTypeOfContainerExists(newContainerTypeDTO.getName(), newContainerTypeDTO.getNumberOfCalories());
        checkTypeOfContainerName(newContainerTypeDTO.getName());
        checkTypeOfContainerNumberOfCalories(newContainerTypeDTO.getNumberOfCalories());
        ContainerType containerType = modelMapper.map(newContainerTypeDTO, ContainerType.class);
        typeOfContainerRepository.save(containerType);
        log.info("Type of container with name and number of calories successfully created");
    }

    public ContainerTypeDTO getTypeOfContainer(long id) {
        log.info("Getting type of container with id {}: ", id);
        ContainerType containerType = getTypeOfContainerIfExists(id, CRUDOperations.READ);
        ContainerTypeDTO containerTypeDTO = modelMapper.map(containerType, ContainerTypeDTO.class);
        log.info("Type of container found: {}: ", containerTypeDTO);
        return containerTypeDTO;
    }

    @SneakyThrows
    public void updateTypeOfContainer(long id, String updatedTypeOfContainerJson) {
        ContainerTypeForUpdateDTO updatedTypeOfContainerDTODTO = objectMapper.readValue(updatedTypeOfContainerJson, ContainerTypeForUpdateDTO.class);
        ContainerType containerTypeToUpdate = getTypeOfContainerIfExists(id, CRUDOperations.UPDATE);
        log.info("Updating type of container with id {} with new data {}: ", id, updatedTypeOfContainerDTODTO);
        checkTypeOfContainerNameExistence(updatedTypeOfContainerDTODTO.getName());
        checkTypeOfContainerName(updatedTypeOfContainerDTODTO.getName());
        ContainerType updatedContainerType = modelMapper.map(updatedTypeOfContainerDTODTO, ContainerType.class);
        ContainerType containerTypeWithNewParameters = updateTypeOfContainerOptions(containerTypeToUpdate, updatedContainerType);
        typeOfContainerRepository.save(containerTypeWithNewParameters);
        log.info("Type of container with id {} successfully updated", id);
    }

    public void deleteTypeOfContainer(long id) {
        log.info("Deleting type of container with id: {}", id);
        checkTypeOfContainerExistence(id);
        typeOfContainerRepository.deleteById(id);
        log.info("Type of container with id {} successfully deleted", id);
    }

    public boolean isContainerTypeExists(String name) {
        return typeOfContainerRepository.getByName(name) != null;
    }

    public ContainerType getTypeOfContainerByName(String name) {
        return typeOfContainerRepository.getByName(name);
    }

    private boolean isTypeOfContainerExists(long caloricContent) {
        return typeOfContainerRepository.existsById(caloricContent);
    }

    private ContainerType getTypeOfContainerIfExists(long id, CRUDOperations operation) {
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
        if (isContainerTypeExists(typeOfContainerName)) {
            log.error("The attempt to create a new type of container failed, a type of container with name {} already exists", typeOfContainerName);
            throw new ConflictBetweenData("Type of container with name " + typeOfContainerName + " already exists");
        }
        if (isTypeOfContainerExists(typeOfContainerNumberOfCalories)) {
            log.error("The attempt to create a new type of container failed, a type of container with number of calories {} already exists", typeOfContainerNumberOfCalories);
            throw new ConflictBetweenData("Type of container with number of calories " + typeOfContainerNumberOfCalories + " already exists");
        }
    }

    private void checkTypeOfContainerNameExistence(String typeOfContainerName) {
        if (isContainerTypeExists(typeOfContainerName)) {
            log.error("The attempt to create a new type of container failed, a type of container with name {} already exists", typeOfContainerName);
            throw new ConflictBetweenData("Type of container with name " + typeOfContainerName + " already exists");
        }
    }

    private ContainerType updateTypeOfContainerOptions(ContainerType containerType, ContainerType updatedContainerType) {
        containerType.setPrice(updatedContainerType.getPrice());
        containerType.setName(updatedContainerType.getName());
        return containerType;
    }
}
