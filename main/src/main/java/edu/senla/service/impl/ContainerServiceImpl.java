package edu.senla.service.impl;

import edu.senla.dao.DishRepository;
import edu.senla.dao.TypeOfContainerRepository;
import edu.senla.exeption.BadRequest;
import edu.senla.exeption.NotFound;
import edu.senla.model.dto.ContainerComponentsDTO;
import edu.senla.model.dto.ContainerComponentsNamesDTO;
import edu.senla.model.dto.ContainerComponentsParamsDTO;
import edu.senla.model.entity.Container;
import edu.senla.model.entity.Order;
import edu.senla.model.enums.DishType;
import edu.senla.service.ContainerService;
import edu.senla.service.DishService;
import edu.senla.service.ContainerTypeService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
@Log4j2
public class ContainerServiceImpl extends AbstractService implements ContainerService {

    private final DishService dishService;
    private final ContainerTypeService containerTypeService;
    private final DishRepository dishRepository;
    private final TypeOfContainerRepository typeOfContainerRepository;
    private static final double percentageOfMeatByTotalWeight = 0.2;
    private static final double percentageOfGarnishByTotalWeight = 0.4;
    private static final double percentageOfSaladByTotalWeight = 0.3;
    private static final double percentageOfSauceByTotalWeight = 0.1;

    public List<ContainerComponentsDTO> filterContainers(List<ContainerComponentsDTO> containers) {
        return containers.stream().filter(this::isContainerComponentsCorrect).collect(Collectors.toList());
    }

    public double calculateTotalOrderCost(List<Container> containers) {
        return containers.stream()
                .map(c -> typeOfContainerRepository.getPriceByName(c.getContainerType().getName())).mapToDouble(Double::doubleValue).sum();
    }

    @SneakyThrows
    public ContainerComponentsParamsDTO calculateWeightOfDishes(String containerComponentsJson) {
        ContainerComponentsDTO containerComponentsDTO = objectMapper.readValue(containerComponentsJson, ContainerComponentsDTO.class);
        if (!isContainerComponentsCorrect(containerComponentsDTO))
            throw new NotFound("There is no such type of container or non-existent dish found in container");
        if (!dishService.isAllDishesHaveDishInformation(containerComponentsDTO))
            throw new BadRequest("There is not enough information about the dishes to calculate");
        ContainerComponentsParamsDTO containerComponentsParamsDTO = calculateWeightAndCaloricContent(containerComponentsDTO);
        log.info("Calculated the weight of the dishes for the container size {} : {}", containerComponentsDTO.getTypeOfContainer(), containerComponentsParamsDTO);
        return containerComponentsParamsDTO;
    }

    public ContainerComponentsNamesDTO mapFromContainerEntityToContainerComponentsNamesDTO(Container container) {
        ContainerComponentsNamesDTO containerComponentsNamesDTO = new ContainerComponentsNamesDTO();
        /*containerComponentsNamesDTO.setMeat(dishRepository.getNameById(container.getMeat()));
        containerComponentsNamesDTO.setGarnish(dishRepository.getNameById(container.getGarnish()));
        containerComponentsNamesDTO.setSalad(dishRepository.getNameById(container.getSalad()));
        containerComponentsNamesDTO.setSauce(dishRepository.getNameById(container.getSauce()));
        containerComponentsNamesDTO.setTypeOfContainer(typeOfContainerRepository.getNameById(container.getContainerType().getNumberOfCalories()));*/
        return containerComponentsNamesDTO;
    }

    public Container mapFromContainerComponentsDTOToContainerEntity(ContainerComponentsDTO containerComponentsDTO, Order order) {
        Container container = modelMapper.map(containerComponentsDTO, Container.class);
        container.setContainerType(containerTypeService.getTypeOfContainerByName(containerComponentsDTO.getTypeOfContainer()));
        container.setOrder(order);
        return container;
    }

    private boolean isContainerComponentsCorrect(ContainerComponentsDTO containerComponentsDTO) {
        return containerTypeService.isContainerTypeExists(containerComponentsDTO.getTypeOfContainer()) && isContainerFilledCorrectly(containerComponentsDTO);
    }

    private ContainerComponentsParamsDTO calculateWeightAndCaloricContent(ContainerComponentsDTO containerComponentsDTO) {
        ContainerComponentsParamsDTO containerComponentsParamsDTO = new ContainerComponentsParamsDTO();
        int numberOfCalories = 0;
        //int numberOfCalories = (int) typeOfContainerRepository.getByName(containerComponentsDTO.getTypeOfContainer()).getNumberOfCalories();
        containerComponentsParamsDTO.setTotalCaloricContent(Math.round(numberOfCalories));
        double meatCaloricContentIn100Grams = dishRepository.getById(containerComponentsDTO.getMeat()).getDishInformation().getCaloricContent();
        double garnishCaloricContentIn100Grams = dishRepository.getById(containerComponentsDTO.getGarnish()).getDishInformation().getCaloricContent();
        double saladCaloricContentIn100Grams = dishRepository.getById(containerComponentsDTO.getSalad()).getDishInformation().getCaloricContent();
        double sauceCaloricContentIn100Grams = dishRepository.getById(containerComponentsDTO.getSauce()).getDishInformation().getCaloricContent();
        double totalWeight = 100 * (numberOfCalories / (percentageOfMeatByTotalWeight * meatCaloricContentIn100Grams
                + percentageOfGarnishByTotalWeight * garnishCaloricContentIn100Grams + percentageOfSaladByTotalWeight * saladCaloricContentIn100Grams
                + percentageOfSauceByTotalWeight * sauceCaloricContentIn100Grams));
        calculateAndSetDishesWeight(totalWeight, containerComponentsParamsDTO);
        calculateAndSetDishesCaloricContent(meatCaloricContentIn100Grams, garnishCaloricContentIn100Grams,
                saladCaloricContentIn100Grams, sauceCaloricContentIn100Grams, containerComponentsParamsDTO);
        return containerComponentsParamsDTO;
    }

    private void calculateAndSetDishesWeight(double totalWeight, ContainerComponentsParamsDTO containerComponentsParamsDTO) {
        containerComponentsParamsDTO.setMeatWeight(Math.round(percentageOfMeatByTotalWeight * totalWeight));
        containerComponentsParamsDTO.setGarnishWeight(Math.round(percentageOfGarnishByTotalWeight * totalWeight));
        containerComponentsParamsDTO.setSaladWeight(Math.round(percentageOfSaladByTotalWeight * totalWeight));
        containerComponentsParamsDTO.setSauceWeight(Math.round(percentageOfSauceByTotalWeight * totalWeight));
        containerComponentsParamsDTO.setTotalWeight(Math.round(totalWeight));
    }

    private void calculateAndSetDishesCaloricContent(double meatCaloricContentIn100Grams, double garnishCaloricContentIn100Grams,
                                                     double saladCaloricContentIn100Grams, double sauceCaloricContentIn100Grams,
                                                     ContainerComponentsParamsDTO containerComponentsParamsDTO) {
        double meatWeight = containerComponentsParamsDTO.getMeatWeight();
        double garnishWeight = containerComponentsParamsDTO.getGarnishWeight();
        double saladWeight = containerComponentsParamsDTO.getSaladWeight();
        double sauceWeight = containerComponentsParamsDTO.getSauceWeight();
        containerComponentsParamsDTO.setMeatCaloricContent(Math.round(calculateCaloricContentOfDish(meatWeight, meatCaloricContentIn100Grams)));
        containerComponentsParamsDTO.setGarnishCaloricContent(Math.round(calculateCaloricContentOfDish(garnishWeight, garnishCaloricContentIn100Grams)));
        containerComponentsParamsDTO.setSaladCaloricContent(Math.round(calculateCaloricContentOfDish(saladWeight, saladCaloricContentIn100Grams)));
        containerComponentsParamsDTO.setSauceCaloricContent(Math.round(calculateCaloricContentOfDish(sauceWeight, sauceCaloricContentIn100Grams)));
    }

    private boolean isContainerFilledCorrectly(ContainerComponentsDTO containerComponentsDTO) {
        try {
            boolean isMeatTypeCorrect = dishRepository.getById(containerComponentsDTO.getMeat()).getType().equals(DishType.MEAT);
            boolean isGarnishTypeCorrect = dishRepository.getById(containerComponentsDTO.getGarnish()).getType().equals(DishType.GARNISH);
            boolean isSaladTypeCorrect = dishRepository.getById(containerComponentsDTO.getSalad()).getType().equals(DishType.SALAD);
            boolean isSauceTypeCorrect = dishRepository.getById(containerComponentsDTO.getSauce()).getType().equals(DishType.SAUCE);
            return isMeatTypeCorrect && isGarnishTypeCorrect && isSaladTypeCorrect && isSauceTypeCorrect;
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private double calculateCaloricContentOfDish(double dishWeight, double dishCaloricContentIn100Grams) {
        return 0.01 * dishWeight * dishCaloricContentIn100Grams;
    }
}
