package edu.senla.service.impl;

import edu.senla.dao.DishInformationRepository;
import edu.senla.dao.DishRepository;
import edu.senla.exeption.ConflictBetweenData;
import edu.senla.exeption.NotFound;
import edu.senla.model.dto.DishInformationDTO;
import edu.senla.model.dto.DishInformationForUpdateDTO;
import edu.senla.model.entity.Dish;
import edu.senla.model.entity.DishInformation;
import edu.senla.model.enums.CRUDOperations;
import edu.senla.service.DishInformationService;
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
public class DishInformationServiceImpl extends AbstractService implements DishInformationService {

    private final DishServiceImpl dishService;
    private final DishInformationRepository dishInformationRepository;
    private final DishRepository dishRepository;

    public List<DishInformationDTO> getAllDishesInformation(int pages) {
        log.info("Getting all dishes information");
        Page<DishInformation> dishesInformation = dishInformationRepository.findAll(PageRequest.of(0, 10, Sort.by("caloricContent").descending()));
        return dishesInformation.stream().map(d -> modelMapper.map(d, DishInformationDTO.class)).toList();
    }

    @SneakyThrows
    public void createDishInformation(String dishInformationJson) {
        DishInformationDTO newDishInformationDTO = objectMapper.readValue(dishInformationJson, DishInformationDTO.class);
        log.info("Creating new dish information: {}", newDishInformationDTO);
        checkDishInformation(newDishInformationDTO);
        DishInformation newDishInformation = modelMapper.map(newDishInformationDTO, DishInformation.class);
        Dish dish = dishRepository.getById(newDishInformationDTO.getDishId());
        dish.setDishInformation(dishInformationRepository.saveAndFlush(newDishInformation));
        dishRepository.save(dish);
        log.info("Dish information for dish with id {} successfully created", newDishInformationDTO.getDishId());
    }

    public DishInformationDTO getDishInformation(long id) {
        log.info("Getting dish info with id {}: ", id);
        DishInformation dishInformation = getDishInformationIfExists(id, CRUDOperations.READ);
        DishInformationDTO dishInformationDTO = modelMapper.map(dishInformation, DishInformationDTO.class);
        log.info("Dish info found: {}: ", dishInformationDTO);
        return dishInformationDTO;
    }

    @SneakyThrows
    public void updateDishInformation(long id, String updatedDishInformationJson) {
        DishInformationForUpdateDTO updatedDishInformationDTO = objectMapper.readValue(updatedDishInformationJson, DishInformationForUpdateDTO.class);
        DishInformation dishInformationToUpdate = getDishInformationIfExists(id, CRUDOperations.UPDATE);
        log.info("Updating dish information with id {} with new data {}: ", id, updatedDishInformationDTO);
        DishInformation updatedDishInformation = modelMapper.map(updatedDishInformationDTO, DishInformation.class);
        DishInformation dishInformationWithNewParameters = updateDishInformationOptions(dishInformationToUpdate, updatedDishInformation);
        dishInformationRepository.save(dishInformationWithNewParameters);
        log.info("Dish information with id {} successfully updated", id);
    }

    public void deleteDishInformation(long id) {
        log.info("Deleting dish information with id: {}", id);
        checkDishInformationExistent(id);
        dishInformationRepository.deleteById(id);
        log.info("Dish information with id {} successfully deleted", id);
    }

    private DishInformation getDishInformationIfExists(long id, CRUDOperations operation) {
        if (!dishInformationRepository.existsById(id)) {
            log.info("The attempt to {} a dish information failed, there is no dish information with id {}", operation.toString().toLowerCase(), id);
            throw new NotFound("There is no dish information with id " + id);
        }
        return dishInformationRepository.getById(id);
    }

    private void checkDishInformationExistent(long id) {
        if (!dishInformationRepository.existsById(id)) {
            log.info("The attempt to delete a dish information failed, there is no dish information with id {}", id);
            throw new NotFound("There is no dish information with id " + id);
        }
    }

    private void checkDishInformation(DishInformationDTO newDishInformationDTO) {
        if (!dishRepository.existsById(newDishInformationDTO.getDishId())) {
            log.info("The attempt to create a dish failed, dish for which the information is being created with id {} not found", newDishInformationDTO.getDishId());
            throw new NotFound("Dish for which the information is being created is not found");
        }
        if (dishService.isDishHasDishInformation(newDishInformationDTO.getDishId())) {
            log.info("The attempt to create a dish failed, dish information for dish with id {} is already exists", newDishInformationDTO.getDishId());
            throw new ConflictBetweenData("Dish information for dish is already exists");
        }
    }

    private DishInformation updateDishInformationOptions(DishInformation dishInformation, DishInformation updatedDishInformation) {
        dishInformation.setDescription(updatedDishInformation.getDescription());
        dishInformation.setProteins(updatedDishInformation.getProteins());
        dishInformation.setFats(updatedDishInformation.getFats());
        dishInformation.setCarbohydrates(updatedDishInformation.getCarbohydrates());
        dishInformation.setCaloricContent(updatedDishInformation.getCaloricContent());
        return dishInformation;
    }
}
