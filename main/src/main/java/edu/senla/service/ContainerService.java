package edu.senla.service;

import ch.qos.logback.classic.Logger;
import edu.senla.dao.*;
import edu.senla.dto.ContainerComponentsDTO;
import edu.senla.dto.ContainerComponentsNamesDTO;
import edu.senla.dto.ContainerComponentsParamsDTO;
import edu.senla.entity.Container;
import edu.senla.entity.Order;
import edu.senla.enums.DishType;
import edu.senla.exeptions.BadRequest;
import edu.senla.exeptions.NotFound;
import edu.senla.service.serviceinterface.ContainerServiceInterface;
import edu.senla.service.serviceinterface.DishServiceInterface;
import edu.senla.service.serviceinterface.TypeOfContainerServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ContainerService implements ContainerServiceInterface {

    private final DishServiceInterface dishService;

    private final TypeOfContainerServiceInterface typeOfContainerService;

    private final DishRepositoryInterface dishRepository;

    private final TypeOfContainerRepositoryInterface typeOfContainerRepository;

    private final ModelMapper mapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(ClientService.class);

    private final double percentageOfMeatByTotalWeight = 0.2;

    private final double percentageOfGarnishByTotalWeight = 0.4;

    private final double percentageOfSaladByTotalWeight = 0.3;

    private final double percentageOfSauceByTotalWeight = 0.1;

    public List<ContainerComponentsDTO> filterContainers(List<ContainerComponentsDTO> containers) {
        return containers.stream().filter(this::isContainerComponentsCorrect).collect(Collectors.toList());
    }

    public double calculateTotalOrderCost(List<Container> containers) {
        return containers.stream()
                .map(c -> typeOfContainerRepository.getPriceByName(c.getTypeOfContainer().getName())).mapToDouble(Double::doubleValue).sum();
    }

    public ContainerComponentsParamsDTO calculateWeightOfDishes(ContainerComponentsDTO containerComponentsDTO) {
        if (!isContainerComponentsCorrect(containerComponentsDTO)) throw new NotFound("There is no such type of container or non-existent dish found in container");
        if (!dishService.isAllDishesHaveDishInformation(containerComponentsDTO)) throw new BadRequest("There is not enough information about the dishes to calculate");
        ContainerComponentsParamsDTO containerComponentsParamsDTO = calculateWeightAndCaloricContent(containerComponentsDTO);
        LOG.info("Calculated the weight of the dishes for the container size {} : {}", containerComponentsDTO.getTypeOfContainer(), containerComponentsParamsDTO);
        return containerComponentsParamsDTO;
    }

    public ContainerComponentsNamesDTO mapFromContainerEntityToContainerComponentsNamesDTO(Container container) {
        ContainerComponentsNamesDTO containerComponentsNamesDTO = new ContainerComponentsNamesDTO();
        containerComponentsNamesDTO.setMeat(dishRepository.getNameById(container.getMeat()));
        containerComponentsNamesDTO.setGarnish(dishRepository.getNameById(container.getGarnish()));
        containerComponentsNamesDTO.setSalad(dishRepository.getNameById(container.getSalad()));
        containerComponentsNamesDTO.setSauce(dishRepository.getNameById(container.getSauce()));
        containerComponentsNamesDTO.setTypeOfContainer(typeOfContainerRepository.getNameById(container.getTypeOfContainer().getNumberOfCalories()));
        return containerComponentsNamesDTO;
    }

    public Container mapFromContainerComponentsDTOToContainerEntity(ContainerComponentsDTO containerComponentsDTO, Order order) {
        Container container = mapper.map(containerComponentsDTO, Container.class);
        container.setTypeOfContainer(typeOfContainerService.getTypeOfContainerByName(containerComponentsDTO.getTypeOfContainer()));
        container.setOrder(order);
        return container;
    }

    private boolean isContainerComponentsCorrect(ContainerComponentsDTO containerComponentsDTO) {
        return typeOfContainerService.isTypeOfContainerExists(containerComponentsDTO.getTypeOfContainer()) && isContainerFilledCorrectly(containerComponentsDTO);
    }

    private ContainerComponentsParamsDTO calculateWeightAndCaloricContent(ContainerComponentsDTO containerComponentsDTO) {
        ContainerComponentsParamsDTO containerComponentsParamsDTO = new ContainerComponentsParamsDTO();
        int numberOfCalories = (int) typeOfContainerRepository.getByName(containerComponentsDTO.getTypeOfContainer()).getNumberOfCalories();
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
            boolean isMeatTypeCorrect = dishRepository.getById(containerComponentsDTO.getMeat()).getDishType().equals(DishType.MEAT);
            boolean isGarnishTypeCorrect = dishRepository.getById(containerComponentsDTO.getGarnish()).getDishType().equals(DishType.GARNISH);
            boolean isSaladTypeCorrect = dishRepository.getById(containerComponentsDTO.getSalad()).getDishType().equals(DishType.SALAD);
            boolean isSauceTypeCorrect = dishRepository.getById(containerComponentsDTO.getSauce()).getDishType().equals(DishType.SAUCE);
            return isMeatTypeCorrect && isGarnishTypeCorrect && isSaladTypeCorrect && isSauceTypeCorrect;
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private double calculateCaloricContentOfDish(double dishWeight, double dishCaloricContentIn100Grams) {
        return 0.01 * dishWeight * dishCaloricContentIn100Grams;
    }

}
