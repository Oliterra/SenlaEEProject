package edu.senla.service;

import ch.qos.logback.classic.Logger;
import edu.senla.dao.DishRepositoryInterface;
import edu.senla.dto.ContainerComponentsDTO;
import edu.senla.dto.DishDTO;
import edu.senla.entity.Dish;
import edu.senla.enums.CRUDOperations;
import edu.senla.enums.DishType;
import edu.senla.exeptions.BadRequest;
import edu.senla.exeptions.ConflictBetweenData;
import edu.senla.exeptions.NotFound;
import edu.senla.service.serviceinterface.DishServiceInterface;
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
public class DishService implements DishServiceInterface {

    private final DishRepositoryInterface dishRepository;

    private final ValidationServiceInterface validationService;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(DishService.class);

    private final ModelMapper mapper;

    public List<DishDTO> getAllDishes() {
        LOG.info("Getting all dishes");
        Page<Dish> dishes = dishRepository.findAll(PageRequest.of(0, 10, Sort.by("name").descending()));
        return dishes.stream().map(d -> mapper.map(d, DishDTO.class)).toList();
    }

    public void createDish(DishDTO newDishDTO) {
        LOG.info("A request to create a dish {} was received", newDishDTO);
        checkDishDTOName(newDishDTO, CRUDOperations.CREATE);
        Dish dish = setDishDTOTypeToDishEntity(newDishDTO, CRUDOperations.CREATE);
        dishRepository.save(dish);
        LOG.info("Dish with name {} and type {} successfully created", dish.getName(), dish.getDishType());
    }

    public DishDTO getDish(long id) {
        LOG.info("Getting dish with id {}: ", id);
        Dish dish = getDishIfExists(id, CRUDOperations.READ);
        DishDTO dishDTO = mapper.map(dish, DishDTO.class);
        dishDTO.setDishType(dish.getDishType().toString().toLowerCase());
        LOG.info("Dish found: {}: ", dishDTO);
        return dishDTO;
    }

    public void updateDish(long id, DishDTO updatedDishDTO) {
        Dish dishToUpdate = getDishIfExists(id, CRUDOperations.UPDATE);
        LOG.info("Updating dish with id {} with new data {}: ", id, updatedDishDTO);
        checkDishDTOName(updatedDishDTO, CRUDOperations.UPDATE);
        Dish dishWithUpdatedOptions = updateDishOptions(dishToUpdate, updatedDishDTO);
        dishRepository.save(dishWithUpdatedOptions);
        LOG.info("Dish with id {} successfully updated", id);
    }

    public void deleteDish(long id) {
        LOG.info("Deleting dish with id: {}", id);
        Dish dishToDelete = getDishIfExists(id, CRUDOperations.DELETE);
        dishRepository.deleteById(id);
        LOG.info("Dish with id {} successfully deleted", id);
    }

    private boolean isDishExists(String name) {
        return dishRepository.getByName(name) != null;
    }

    public boolean isDishHasDishInformation(long id) {
        return dishRepository.getById(id).getDishInformation() != null;
    }

    public boolean isAllDishesHaveDishInformation(ContainerComponentsDTO containerComponentsDTO) {
        long meat = containerComponentsDTO.getMeat();
        long garnish = containerComponentsDTO.getGarnish();
        long salad = containerComponentsDTO.getSalad();
        long sauce = containerComponentsDTO.getSauce();
        return isDishHasDishInformation(meat) && isDishHasDishInformation(garnish) && isDishHasDishInformation(salad) && isDishHasDishInformation(sauce);
    }

    private Dish setDishDTOTypeToDishEntity(DishDTO newDishDTO, CRUDOperations operation) {
        Dish dish = mapper.map(newDishDTO, Dish.class);
        DishType dishType = translateDishType(newDishDTO.getDishType(), operation);
        dish.setDishType(dishType);
        return dish;
    }

    private Dish getDishIfExists(long id, CRUDOperations operation){
        if (!dishRepository.existsById(id)) {
            LOG.info("The attempt to {} a dish failed, there is no dish with id {}", operation.toString().toLowerCase(), id);
            throw new NotFound("There is no dish with id " + id);
        }
        return dishRepository.getById(id);
    }

    private void checkDishDTOName(DishDTO newDishDTO, CRUDOperations operation){
        String newDishName = newDishDTO.getName();
        if (isDishExists(newDishName)) {
            LOG.error("The attempt to {} a dish failed, a dish with name {} already exists", operation.toString().toLowerCase(), newDishName);
            throw new ConflictBetweenData("Dish with name " + newDishName + " already exists");
        }
        if (!validationService.isNameCorrect(newDishName)) {
            LOG.error("The attempt to {} a dish failed, a dish name {} contains invalid characters", operation.toString().toLowerCase(), newDishName);
            throw new BadRequest("Dish name " + newDishName + " contains invalid characters");
        }
        if (!validationService.isNameLengthValid(newDishName)) {
            LOG.error("The attempt to {} a dish failed, a dish name {} is to short", operation.toString().toLowerCase(), newDishName);
            throw new BadRequest("Dish name " + newDishName + " is too short");
        }
    }

    private DishType translateDishType(String dishType, CRUDOperations operation) {
        return switch (dishType) {
            case "meat" -> DishType.MEAT;
            case "garnish" -> DishType.GARNISH;
            case "salad" -> DishType.SALAD;
            case "sauce" -> DishType.SAUCE;
            default -> {
                LOG.error("The attempt to {} a dish failed, a dish type {} invalid", operation.toString().toLowerCase(), dishType);
                throw new BadRequest("Dish type " + dishType + " invalid");
            }
        };
    }

    private Dish updateDishOptions(Dish dishToUpdate, DishDTO updatedDishDTO) {
        dishToUpdate.setName(updatedDishDTO.getName());
        dishToUpdate.setDishType(translateDishType(updatedDishDTO.getDishType(), CRUDOperations.UPDATE));
        return dishToUpdate;
    }

}
