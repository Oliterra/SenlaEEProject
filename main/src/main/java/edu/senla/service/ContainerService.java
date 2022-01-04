package edu.senla.service;

import edu.senla.dao.*;
import edu.senla.dto.*;
import edu.senla.entity.Client;
import edu.senla.entity.Container;
import edu.senla.entity.Order;
import edu.senla.service.serviceinterface.ContainerServiceInterface;
import edu.senla.service.serviceinterface.TypeOfContainerServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ContainerService implements ContainerServiceInterface {

    private final TypeOfContainerServiceInterface typeOfContainerService;

    private final OrderRepositoryInterface orderRepository;

    private final ClientRepositoryInterface clientRepository;

    private final DishRepositoryInterface dishRepository;

    private final ContainerRepositoryInterface containerRepository;

    private final TypeOfContainerRepositoryInterface typeOfContainerRepository;

    private final ModelMapper mapper;

    private final double percentageOfMeatByTotalWeight = 0.2;

    private final double percentageOfGarnishByTotalWeight = 0.4;

    private final double percentageOfSaladByTotalWeight = 0.3;

    private final double percentageOfSauceByTotalWeight = 0.1;

    public OrderTotalCostDTO makeOrder(long clientId, ShoppingCartDTO shoppingCartDTO) {
        Order order = new Order();
        Client client = clientRepository.getById(clientId);
        client.setAddress(shoppingCartDTO.getAddress());
        order.setClient(client);
        order.setStatus("new");
        order.setPaymentType(shoppingCartDTO.getPaymentType());
        order.setDate(LocalDate.now());
        order.setTime(LocalTime.now());
        orderRepository.saveAndFlush(order);
        List<Container> containers = shoppingCartDTO.getContainers().stream()
                .map(container -> mapFromContainerComponentsDTOToContainerEntity(container, order))
                .toList();
        containerRepository.saveAll(containers);
        OrderTotalCostDTO orderTotalCostDTO = new OrderTotalCostDTO();
        orderTotalCostDTO.setOrderTotalCost(calculateTotalOrderCost(containers));
        return orderTotalCostDTO;
    }

    public List<ContainerComponentsDTO> filterContainers(List<ContainerComponentsDTO> containers) {
        return containers.stream().filter(this::isContainerComponentsCorrect).collect(Collectors.toList());
    }

    public boolean isPaymentTypeCorrect(String paymentType) {
        return paymentType.equals("cash payment") || paymentType.equals("card payment to courier") || paymentType.equals("card payment online");
    }

    public boolean isContainerComponentsCorrect(ContainerComponentsDTO containerComponentsDTO) {
        return typeOfContainerService.isTypeOfContainerExists(containerComponentsDTO.getTypeOfContainer()) && isContainerFilledCorrectly(containerComponentsDTO);
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

    public double calculateTotalOrderCost(List<Container> containers) {
        return containers.stream()
                .map(c -> typeOfContainerRepository.getPriceByName(c.getTypeOfContainer().getName()))
                .collect(Collectors.summingDouble(Double::doubleValue));
    }

    public ContainerComponentsParamsDTO calculateWeightOfDishes(ContainerComponentsDTO containerComponentsDTO) {
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

    private Container mapFromContainerComponentsDTOToContainerEntity(ContainerComponentsDTO containerComponentsDTO, Order order) {
        Container container = mapper.map(containerComponentsDTO, Container.class);
        container.setTypeOfContainer(typeOfContainerService.getTypeOfContainerByName(containerComponentsDTO.getTypeOfContainer()));
        container.setOrder(order);
        return container;
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

    private boolean isContainerFilledCorrectly(ContainerComponentsDTO containerComponentsDTO){
        try {
            boolean isMeatTypeCorrect = dishRepository.getById(containerComponentsDTO.getMeat()).getDishType().equals("meat");
            boolean isGarnishTypeCorrect = dishRepository.getById(containerComponentsDTO.getGarnish()).getDishType().equals("garnish");
            boolean isSaladTypeCorrect = dishRepository.getById(containerComponentsDTO.getSalad()).getDishType().equals("salad");
            boolean isSauceTypeCorrect = dishRepository.getById(containerComponentsDTO.getSauce()).getDishType().equals("sauce");
            return isMeatTypeCorrect && isGarnishTypeCorrect && isSaladTypeCorrect && isSauceTypeCorrect;
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private double calculateCaloricContentOfDish(double dishWeight, double dishCaloricContentIn100Grams){
        return 0.01 * dishWeight * dishCaloricContentIn100Grams;
    }

}
