package edu.senla.service;

import ch.qos.logback.classic.Logger;
import edu.senla.dao.DishInformationRepositoryInterface;
import edu.senla.dao.DishRepositoryInterface;
import edu.senla.dto.DishInformationDTO;
import edu.senla.dto.DishInformationForUpdateDTO;
import edu.senla.entity.Dish;
import edu.senla.entity.DishInformation;
import edu.senla.enums.CRUDOperations;
import edu.senla.exeptions.ConflictBetweenData;
import edu.senla.exeptions.NotFound;
import edu.senla.service.serviceinterface.DishInformationServiceInterface;
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
public class DishInformationService implements DishInformationServiceInterface {

    private final DishService dishService;

    private final DishInformationRepositoryInterface dishInformationRepository;

    private final DishRepositoryInterface dishRepository;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(DishInformationService.class);

    private final ModelMapper mapper;

    public List<DishInformationDTO> getAllDishesInformation() {
        LOG.info("Getting all dishes information");
        Page<DishInformation> dishesInformation = dishInformationRepository.findAll(PageRequest.of(0, 10, Sort.by("caloricContent").descending()));
        return dishesInformation.stream().map(d -> mapper.map(d, DishInformationDTO.class)).toList();
    }

    public void createDishInformation(DishInformationDTO newDishInformationDTO) {
        LOG.info("Creating new dish information: {}", newDishInformationDTO);
        checkDishInformation(newDishInformationDTO);
        DishInformation newDishInformation = mapper.map(newDishInformationDTO, DishInformation.class);
        Dish dish = dishRepository.getById(newDishInformationDTO.getDishId());
        dish.setDishInformation(dishInformationRepository.saveAndFlush(newDishInformation));
        dishRepository.save(dish);
        LOG.info("Dish information for dish with id {} successfully created", newDishInformationDTO.getDishId());
    }

    public DishInformationDTO getDishInformation(long id) {
        LOG.info("Getting dish info with id {}: ", id);
        DishInformation dishInformation = getDishInformationIfExists(id, CRUDOperations.READ);
        DishInformationDTO dishInformationDTO = mapper.map(dishInformation, DishInformationDTO.class);
        LOG.info("Dish info found: {}: ", dishInformationDTO);
        return dishInformationDTO;
    }

    public void updateDishInformation(long id, DishInformationForUpdateDTO updatedDishInformationDTO) {
        DishInformation dishInformationToUpdate = getDishInformationIfExists(id, CRUDOperations.UPDATE);
        LOG.info("Updating dish information with id {} with new data {}: ", id, updatedDishInformationDTO);
        DishInformation updatedDishInformation = mapper.map(updatedDishInformationDTO, DishInformation.class);
        DishInformation dishInformationWithNewParameters = updateDishInformationOptions(dishInformationToUpdate, updatedDishInformation);
        dishInformationRepository.save(dishInformationWithNewParameters);
        LOG.info("Dish information with id {} successfully updated", id);
    }

    public void deleteDishInformation(long id) {
        LOG.info("Deleting dish information with id: {}", id);
        DishInformation dishInformationToDelete = getDishInformationIfExists(id, CRUDOperations.DELETE);
        dishInformationRepository.deleteById(id);
        LOG.info("Dish information with id {} successfully deleted", id);
    }

    private DishInformation getDishInformationIfExists(long id, CRUDOperations operation){
        if (!dishInformationRepository.existsById(id)) {
            LOG.info("The attempt to {} a dish information failed, there is no dish information with id {}", operation.toString().toLowerCase(), id);
            throw new NotFound("There is no dish information with id " + id);
        }
        return dishInformationRepository.getById(id);
    }

    private void checkDishInformation(DishInformationDTO newDishInformationDTO){
        if (!dishRepository.existsById(newDishInformationDTO.getDishId())) {
            LOG.info("The attempt to create a dish failed, dish for which the information is being created with id {} not found", newDishInformationDTO.getDishId());
            throw new NotFound("Dish for which the information is being created is not found");
        }
        if (dishService.isDishHasDishInformation(newDishInformationDTO.getDishId())) {
            LOG.info("The attempt to create a dish failed, dish information for dish with id {} is already exists", newDishInformationDTO.getDishId());
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
