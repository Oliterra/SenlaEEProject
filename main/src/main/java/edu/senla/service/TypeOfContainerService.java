package edu.senla.service;

import ch.qos.logback.classic.Logger;
import edu.senla.dao.TypeOfContainerRepositoryInterface;
import edu.senla.dto.TypeOfContainerDTO;
import edu.senla.dto.TypeOfContainerForUpdateDTO;
import edu.senla.entity.TypeOfContainer;
import edu.senla.enums.CRUDOperations;
import edu.senla.exeptions.BadRequest;
import edu.senla.exeptions.ConflictBetweenData;
import edu.senla.exeptions.NotFound;
import edu.senla.service.serviceinterface.TypeOfContainerServiceInterface;
import edu.senla.service.serviceinterface.ValidationServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
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

    private final ValidationServiceInterface validationService;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(TypeOfContainerService.class);

    private final ModelMapper mapper;

    public List<TypeOfContainerDTO> getAllTypesOfContainer() {
        LOG.info("Getting all types of container");
        Page<TypeOfContainer> typeOfContainers = typeOfContainerRepository.findAll(PageRequest.of(0, 10, Sort.by("numberOfCalories").descending()));
        return typeOfContainers.stream().map(t -> mapper.map(t, TypeOfContainerDTO.class)).toList();
    }

    public void createTypeOfContainer(TypeOfContainerDTO newTypeOfContainerDTO) {
        LOG.info("A request to create a type of container {} was received", newTypeOfContainerDTO);
        isTypeOfContainerExists(newTypeOfContainerDTO.getName(), newTypeOfContainerDTO.getNumberOfCalories(), CRUDOperations.CREATE);
        checkTypeOfContainerName(newTypeOfContainerDTO.getName(), CRUDOperations.CREATE);
        checkTypeOfContainerNumberOfCalories(newTypeOfContainerDTO.getNumberOfCalories(), CRUDOperations.CREATE);
        TypeOfContainer typeOfContainer = mapper.map(newTypeOfContainerDTO, TypeOfContainer.class);
        typeOfContainerRepository.save(typeOfContainer);
        LOG.info("Type of container with name and number of calories successfully created");
    }

    public TypeOfContainerDTO getTypeOfContainer(long id) {
        LOG.info("Getting type of container with id {}: ", id);
        TypeOfContainer typeOfContainer = getTypeOfContainerIfExists(id, CRUDOperations.READ);
        TypeOfContainerDTO typeOfContainerDTO = mapper.map(typeOfContainer, TypeOfContainerDTO.class);
        LOG.info("Type of container found: {}: ", typeOfContainerDTO);
        return typeOfContainerDTO;
    }

    public void updateTypeOfContainer(long id, TypeOfContainerForUpdateDTO updatedTypeOfContainerDTODTO) {
        TypeOfContainer typeOfContainerToUpdate = getTypeOfContainerIfExists(id, CRUDOperations.UPDATE);
        LOG.info("Updating type of container with id {} with new data {}: ", id, updatedTypeOfContainerDTODTO);
        isTypeOfContainerExists(updatedTypeOfContainerDTODTO.getName(), CRUDOperations.CREATE);
        checkTypeOfContainerName(updatedTypeOfContainerDTODTO.getName(), CRUDOperations.CREATE);
        TypeOfContainer updatedTypeOfContainer = mapper.map(updatedTypeOfContainerDTODTO, TypeOfContainer.class);
        TypeOfContainer typeOfContainerWithNewParameters = updateTypeOfContainerOptions(typeOfContainerToUpdate, updatedTypeOfContainer);
        typeOfContainerRepository.save(typeOfContainerWithNewParameters);
        LOG.info("Type of container with id {} successfully updated", id);
    }

    public void deleteTypeOfContainer(long id) {
        LOG.info("Deleting type of container with id: {}", id);
        TypeOfContainer typeOfContainerToDelete = getTypeOfContainerIfExists(id, CRUDOperations.DELETE);
        typeOfContainerRepository.deleteById(id);
        LOG.info("Type of container with id {} successfully deleted", id);;
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

    private TypeOfContainer getTypeOfContainerIfExists(long id, CRUDOperations operation){
        if (!typeOfContainerRepository.existsById(id)) {
            LOG.info("The attempt to {} a type of container failed, there is no type of container with id {}", operation.toString().toLowerCase(), id);
            throw new NotFound("There is no type of container with id " + id);
        }
        return typeOfContainerRepository.getById(id);
    }

    private void checkTypeOfContainerNumberOfCalories(long typeOfContainerNumberOfCalories, CRUDOperations operation){
        if (typeOfContainerNumberOfCalories < 0 || typeOfContainerNumberOfCalories > 5000) {
            LOG.error("The attempt to {} a new type of container failed, a container number of calories {} invalid", operation.toString().toLowerCase(), typeOfContainerNumberOfCalories);
            throw new BadRequest("Type of container number of calories " + typeOfContainerNumberOfCalories + " is invalid");
        }
    }

    private void checkTypeOfContainerName(String typeOfContainerName, CRUDOperations operation){
        if (!validationService.isTypeOContainerNameCorrect(typeOfContainerName)) {
            LOG.error("The attempt to {} a new type of container failed, a type of container name {} invalid", operation.toString().toLowerCase(), typeOfContainerName);
            throw new BadRequest("Type of container name " + typeOfContainerName + " is invalid");
        }
    }

    private void isTypeOfContainerExists(String typeOfContainerName, long typeOfContainerNumberOfCalories, CRUDOperations operation){
        if (isTypeOfContainerExists(typeOfContainerName)) {
            LOG.error("The attempt to {} a new type of container failed, a type of container with name {} already exists", operation.toString().toLowerCase(), typeOfContainerName);
            throw new ConflictBetweenData("Type of container with name " + typeOfContainerName + " already exists");
        }
        if (isTypeOfContainerExists(typeOfContainerNumberOfCalories)) {
            LOG.error("The attempt to {} a new type of container failed, a type of container with number of calories {} already exists", operation.toString().toLowerCase(), typeOfContainerNumberOfCalories);
            throw new ConflictBetweenData("Type of container with number of calories " + typeOfContainerNumberOfCalories + " already exists");
        }
    }

    private void isTypeOfContainerExists(String typeOfContainerName, CRUDOperations operation){
        if (isTypeOfContainerExists(typeOfContainerName)) {
            LOG.error("The attempt to {}} a new type of container failed, a type of container with name {} already exists", operation.toString().toLowerCase(), typeOfContainerName);
            throw new ConflictBetweenData("Type of container with name " + typeOfContainerName + " already exists");
        }
    }

    private TypeOfContainer updateTypeOfContainerOptions(TypeOfContainer typeOfContainer, TypeOfContainer updatedTypeOfContainer) {
        typeOfContainer.setPrice(updatedTypeOfContainer.getPrice());
        typeOfContainer.setName(updatedTypeOfContainer.getName());
        return typeOfContainer;
    }

}
