package edu.senla.service.impl;

import edu.senla.dao.ClientRepository;
import edu.senla.dao.ContainerRepository;
import edu.senla.dao.CourierRepository;
import edu.senla.dao.OrderRepository;
import edu.senla.exeption.BadRequest;
import edu.senla.exeption.ConflictBetweenData;
import edu.senla.exeption.NotFound;
import edu.senla.model.dto.*;
import edu.senla.model.entity.Client;
import edu.senla.model.entity.Container;
import edu.senla.model.entity.Courier;
import edu.senla.model.entity.Order;
import edu.senla.model.enums.CRUDOperations;
import edu.senla.model.enums.CourierStatus;
import edu.senla.model.enums.OrderStatus;
import edu.senla.service.ContainerService;
import edu.senla.service.CourierService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
@Log4j2
public class CourierServiceImpl extends AbstractService implements CourierService {

    private final ContainerService containerService;
    private final ContainerRepository containerRepository;
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final CourierRepository courierRepository;
    private final PasswordEncoder passwordEncoder;
    private static final double normOfOrdersPerDay = 4;
    private static final double normalPercentageOfOrdersDeliveredOnTime = 75;

    public List<CourierMainInfoDTO> getAllCouriers(int pages) {
        log.info("Getting all couriers");
        Page<Courier> couriers = courierRepository.findAll(PageRequest.of(0, pages, Sort.by("lastName").descending()));
        return couriers.stream().map(c -> modelMapper.map(c, CourierMainInfoDTO.class)).toList();
    }

    public List<CourierBasicInfoDTO> getAllActiveCouriersDTO() {
        List<CourierBasicInfoDTO> courierBasicInfoDTOS = getAllActiveCouriers().stream().map(c -> modelMapper.map(c, CourierBasicInfoDTO.class)).toList();
        if (courierBasicInfoDTOS.isEmpty())
            throw new NotFound("Currently there are no active couriers to assign orders");
        return courierBasicInfoDTOS;
    }

    @SneakyThrows
    public void createCourier(String courierRegistrationRequestJson) {
        CourierRegistrationRequestDTO newCourierDTO = objectMapper.readValue(courierRegistrationRequestJson, CourierRegistrationRequestDTO.class);
        log.info("A new courier wants to register in the service: " + newCourierDTO);
        checkCourierName(newCourierDTO.getFirstName(), CRUDOperations.CREATE);
        checkCourierName(newCourierDTO.getLastName(), CRUDOperations.CREATE);
        isCourierExistsByPhone(newCourierDTO.getPhone(), CRUDOperations.CREATE);
        checkCourierPhone(newCourierDTO.getPhone(), CRUDOperations.CREATE);
        checkCourierPasswordConfirmation(newCourierDTO);
        newCourierDTO.setPassword(passwordEncoder.encode(newCourierDTO.getPassword()));
        Courier courier = modelMapper.map(newCourierDTO, Courier.class);
        courier.setStatus(CourierStatus.INACTIVE);
        Courier savedCourier = courierRepository.save(courier);
        log.info("A new courier is registered in the service: " + savedCourier);
    }

    public CourierMainInfoDTO getCourier(long id) {
        log.info("Getting courier with id {}: ", id);
        checkCourierExistent(id, CRUDOperations.READ);
        CourierMainInfoDTO courierMainInfoDTO = modelMapper.map(courierRepository.getById(id), CourierMainInfoDTO.class);
        log.info("Courier found: {}: ", courierMainInfoDTO);
        return courierMainInfoDTO;
    }

    public CourierBasicInfoDTO getCourierBasicInfo(long id) {
        Courier courier = getCourierIfExists(id, CRUDOperations.READ);
        return modelMapper.map(courier, CourierBasicInfoDTO.class);
    }

    public CourierCurrentOrderInfoDTO getCurrentOrderForCourier(long id) {
        CourierBasicInfoDTO courierBasicInfo = getCourierBasicInfo(id);
        if (!isCourierActiveNow(id)) {
            log.warn("An attempt to get information about the current order for the courier failed, because courier {} {} is inactive now", courierBasicInfo.getFirstName(), courierBasicInfo.getLastName());
            throw new BadRequest("Courier is inactive now");
        }
        CourierCurrentOrderInfoDTO currentOrderInfoDTO = getCourierOrder(id);
        log.info("Current order of courier {} {}: {}", courierBasicInfo.getFirstName(), courierBasicInfo.getLastName(), currentOrderInfoDTO);
        return currentOrderInfoDTO;
    }

    @SneakyThrows
    public CourierFullInfoDTO getCourierByPhoneAndPassword(String authRequestCourierJson) {
        CourierAuthRequestDTO courierAuthRequestDTO = objectMapper.readValue(authRequestCourierJson, CourierAuthRequestDTO.class);
        String phone = courierAuthRequestDTO.getPhone();
        String password = courierAuthRequestDTO.getPassword();
        try {
            Courier courier = courierRepository.getByPhone(phone);
            if (!passwordEncoder.matches(password, courier.getPassword())) throw new BadRequest();
            return modelMapper.map(courier, CourierFullInfoDTO.class);
        } catch (RuntimeException exception) {
            log.error("No courier found with phone {} and password {}", phone, password);
            throw new NotFound("Invalid phone or password");
        }
    }

    @SneakyThrows
    public void updateCourier(long id, String updatedCourierJson) {
        CourierMainInfoDTO courierDTO = objectMapper.readValue(updatedCourierJson, CourierMainInfoDTO.class);
        Courier courierToUpdate = getCourierIfExists(id, CRUDOperations.UPDATE);
        log.info("Updating courier with id {} with new data {}: ", id, courierDTO);
        checkCourierName(courierDTO.getFirstName(), CRUDOperations.UPDATE);
        checkCourierName(courierDTO.getLastName(), CRUDOperations.UPDATE);
        checkCourierPhone(courierDTO.getPhone(), CRUDOperations.UPDATE);
        isCourierExistsByPhone(courierDTO.getPhone(), CRUDOperations.UPDATE);
        Courier updatedCourier = modelMapper.map(courierDTO, Courier.class);
        Courier courierWithNewParameters = updateCouriersOptions(courierToUpdate, updatedCourier);
        courierRepository.save(courierWithNewParameters);
        log.info("Courier with id {} successfully updated", id);
    }

    public void deleteCourier(long id) {
        log.info("Deleting courier with id: {}", id);
        checkCourierExistent(id, CRUDOperations.DELETE);
        courierRepository.deleteById(id);
        log.info("Courier with id {} successfully deleted", id);
    }

    public long getCurrentCourierId() {
        String courierPhone = getCurrentCourierPhone();
        return courierRepository.getByPhone(courierPhone).getId();
    }

    private Courier getCourierIfExists(long id, CRUDOperations operation) {
        if (!courierRepository.existsById(id)) {
            log.info("The attempt to {} a courier failed, there is no courier with id {}", operation.toString().toLowerCase(), id);
            throw new NotFound("There is no courier with id " + id);
        }
        return courierRepository.getById(id);
    }

    private void checkCourierExistent(long id, CRUDOperations operation) {
        if (!courierRepository.existsById(id)) {
            log.info("The attempt to {} a courier failed, there is no courier with id {}", operation.toString().toLowerCase(), id);
            throw new NotFound("There is no courier with id " + id);
        }
    }

    private void isCourierExistsByPhone(String phone, CRUDOperations operation) {
        if (courierRepository.getByPhone(phone) != null) {
            log.info("The attempt to {} a courier failed, because courier with phone {} already exists", operation.toString().toLowerCase(Locale.ROOT), phone);
            throw new ConflictBetweenData("A courier with this " + phone + " already exists ");
        }
    }

    public void changeCourierStatus(long id) {
        Courier courier = courierRepository.getById(id);
        CourierStatus newStatus = courier.getStatus().equals(CourierStatus.INACTIVE) ? CourierStatus.ACTIVE : CourierStatus.INACTIVE;
        courier.setStatus(newStatus);
        log.info("{} {} changed his/her status to {}", courier.getFirstName(), courier.getLastName(), newStatus.toString().toLowerCase(Locale.ROOT));
        courierRepository.save(courier);
    }

    public void assignOrdersToAllActiveCouriers(List<CourierBasicInfoDTO> courierBasicInfoDTOS) {
        List<Order> orders = getAllNewOrders();
        List<Courier> couriers = getAllUnoccupiedCouriers();
        int numberOfAssignedOrders;
        if (orders.size() > couriers.size()) {
            for (int i = 0; i < couriers.size(); i++) {
                setOrderToCourier(couriers.get(i), orders.get(i));
            }
            numberOfAssignedOrders = couriers.size();
        } else {
            for (int i = 0; i < orders.size(); i++) {
                setOrderToCourier(couriers.get(i), orders.get(i));
            }
            numberOfAssignedOrders = orders.size();
        }
        log.info("{} orders assigned to couriers, {} pending", numberOfAssignedOrders, courierBasicInfoDTOS.size() - numberOfAssignedOrders);
    }

    public void assignNewOrdersToCourier(long id) {
        Courier courier = courierRepository.getById(id);
        if (!isCourierActiveNow(id)) {
            log.info("The courier's {} {} attempt to receive a new order failed because this courier is inactive now", courier.getFirstName(), courier.getLastName());
            throw new BadRequest("You are not active right now. Please update your working status to receive new orders.");
        }
        List<Order> newOrders = getAllNewOrders();
        if (isCourierOccupied(courier)) {
            log.info("The courier's {} {} attempt to receive a new order failed because courier already has an order", courier.getFirstName(), courier.getLastName());
            throw new ConflictBetweenData("You already has an order");
        }
        if (newOrders.isEmpty() || isCourierOccupied(courier)) {
            log.info("The courier's {} {} attempt to receive a new order failed because there are no available orders right now", courier.getFirstName(), courier.getLastName());
            throw new NotFound("There are no available orders right now");
        }
        Order earliestOrder = newOrders.get(0);
        setOrderToCourier(courier, earliestOrder);
        log.info("Order with {} assigned to courier {} {}", earliestOrder.getId(), courier.getFirstName(), courier.getLastName());
    }

    public List<CourierOrderInfoDTO> getAllOrdersOfCourier(long courierId) {
        Courier courier = getCourierIfExists(courierId, CRUDOperations.READ);
        log.info("Requested order history for the courier {} {}", courier.getFirstName(), courier.getLastName());
        return orderRepository.getAllByCourier(courier, PageRequest.of(0, 10, Sort.by("date").descending())).stream()
                .filter(o -> o.getStatus().equals(OrderStatus.COMPLETED_LATE) || o.getStatus().equals(OrderStatus.COMPLETED_ON_TIME))
                .map(this::formCourierOrderInfoDTO)
                .toList();
    }

    private boolean isCourierActiveNow(long id) {
        return courierRepository.getById(id).getStatus().equals(CourierStatus.ACTIVE);
    }

    private CourierPerformanceIndicatorDTO calculateCourierPerformanceIndicator(Courier courier) {
        List<CourierOrderInfoDTO> courierOrders = getAllOrdersOfCourier(courier.getId());
        if (courierOrders.isEmpty()) return null;
        int totalNumberOfOrdersDelivered = courierOrders.size();
        int numberOfOrdersDeliveredOnTime = (int) courierOrders.stream().filter(CourierOrderInfoDTO::isOrderDeliveredOnTime).count();
        int numberOfOrdersDeliveredLate = totalNumberOfOrdersDelivered - numberOfOrdersDeliveredOnTime;
        double percentageOfOrdersDeliveredOnTime = ((double) numberOfOrdersDeliveredOnTime / totalNumberOfOrdersDelivered) * 100;
        double numberOfOrdersDeliveredPerDay = calculateNumberOfOrdersDeliveredPerDay(courierOrders);
        return formCourierPerformanceIndicatorDTO(totalNumberOfOrdersDelivered, numberOfOrdersDeliveredOnTime,
                numberOfOrdersDeliveredLate, percentageOfOrdersDeliveredOnTime, numberOfOrdersDeliveredPerDay);
    }

    public CourierPerformanceIndicatorDTO getCourierPerformanceIndicator(long id) {
        Courier courier = getCourierIfExists(id, CRUDOperations.READ);
        log.info("Requested performance indicator of the courier {} {}", courier.getFirstName(), courier.getLastName());
        CourierPerformanceIndicatorDTO courierPerformanceIndicatorDTO = calculateCourierPerformanceIndicator(courier);
        if (courierPerformanceIndicatorDTO == null) {
            log.info("The attempt to get the courier's performance indicator failed because there is no closed orders found for courier {} {}", courier.getFirstName(), courier.getLastName());
            throw new NotFound("No closed orders found for courier to form courier performance indicator");
        }
        return courierPerformanceIndicatorDTO;
    }

    private CourierCurrentOrderInfoDTO getCourierOrder(long id) {
        Courier courier = courierRepository.getById(id);
        Order order = orderRepository.getByCourierAndStatus(courier, OrderStatus.IN_PROCESS);
        if (order == null) {
            log.warn("An attempt to get information about the current order for the courier failed, because, courier {} {} has no current order", courier.getFirstName(), courier.getLastName());
            throw new NotFound("Courier has no current order");
        }
        Client client = clientRepository.getById(order.getClient().getId());
        List<Container> containers = containerRepository.findAllByOrderId(order.getId());
        double orderCost = containerService.calculateTotalOrderCost(containers);
        List<ContainerComponentsNamesDTO> containersCourierInfoDTOs = containers.stream()
                .map(containerService::mapFromContainerEntityToContainerComponentsNamesDTO).toList();
        return formCourierOrderInfoResponseDTO(containersCourierInfoDTOs, client, order, orderCost);
    }

    private void checkCourierName(String name, CRUDOperations operation) {
        if (!validationService.isNameCorrect(name)) {
            log.error("The attempt to {} a courier failed, a courier name {} contains invalid characters", operation.toString().toLowerCase(), name);
            throw new BadRequest("Name " + name + " contains invalid characters");
        }
        if (!validationService.isNameLengthValid(name)) {
            log.error("The attempt to {} a courier failed, a courier name {} is to short", operation.toString().toLowerCase(), name);
            throw new BadRequest("Name " + name + " is too short");
        }
    }

    private void checkCourierPhone(String phone, CRUDOperations operation) {
        if (!validationService.isPhoneCorrect(phone)) {
            log.error("The attempt to {} a courier failed, a client phone {} is invalid", operation.toString().toLowerCase(), phone);
            throw new BadRequest("Phone " + phone + " is invalid");
        }
    }

    private void checkCourierPasswordConfirmation(CourierRegistrationRequestDTO courierRegistrationRequestDTO) {
        if (!(courierRegistrationRequestDTO.getPassword()).equals(courierRegistrationRequestDTO.getPasswordConfirm())) {
            log.info("The attempt to register a courier failed, because passwords {} and {} do not match",
                    courierRegistrationRequestDTO.getPassword(), courierRegistrationRequestDTO.getPasswordConfirm());
            throw new BadRequest("Passwords " + courierRegistrationRequestDTO.getPassword() + " and " + courierRegistrationRequestDTO.getPasswordConfirm() + " do not match");
        }
    }

    private List<Order> getAllNewOrders() {
        return orderRepository.getByStatusOrderByTimeAsc(OrderStatus.NEW);
    }

    private List<Courier> getAllActiveCouriers() {
        return courierRepository.getByStatus(CourierStatus.ACTIVE, PageRequest.of(0, 10, Sort.by("lastName").descending()));
    }

    private List<Courier> getAllUnoccupiedCouriers() {
        return getAllActiveCouriers().stream().filter(c -> !isCourierOccupied(c)).toList();
    }

    private boolean isCourierOccupied(Courier courier) {
        List<Courier> clientsWitOpenOrders = orderRepository.getAllCouriersByStatus(OrderStatus.IN_PROCESS);
        List<Courier> clientsWithClosedOrders = orderRepository.getAllCouriersByStatus(OrderStatus.RECEIPT_CONFIRMED);
        return clientsWitOpenOrders.contains(courier) || clientsWithClosedOrders.contains(courier);
    }

    private void setOrderToCourier(Courier courier, Order order) {
        order.setCourier(courier);
        order.setStatus(OrderStatus.IN_PROCESS);
    }

    private Courier updateCouriersOptions(Courier courier, Courier updatedCourier) {
        courier.setFirstName(updatedCourier.getFirstName());
        courier.setLastName(updatedCourier.getLastName());
        courier.setPhone(updatedCourier.getPhone());
        return courier;
    }

    private String getCurrentCourierPhone() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private CourierCurrentOrderInfoDTO formCourierOrderInfoResponseDTO(List<ContainerComponentsNamesDTO> containersCourierInfoDTOs,
                                                                       Client client, Order order, double orderCost) {
        CourierCurrentOrderInfoDTO courierCurrentOrderInfoDTO = new CourierCurrentOrderInfoDTO();
        courierCurrentOrderInfoDTO.setContainers(containersCourierInfoDTOs);
        courierCurrentOrderInfoDTO.setClientFirstName(client.getFirstName());
        courierCurrentOrderInfoDTO.setClientLastName(client.getLastName());
        courierCurrentOrderInfoDTO.setAddress(client.getAddress());
        courierCurrentOrderInfoDTO.setTime(order.getTime());
        courierCurrentOrderInfoDTO.setPaymentType(order.getPaymentType().toString().toLowerCase(Locale.ROOT));
        courierCurrentOrderInfoDTO.setOrderCost(orderCost);
        return courierCurrentOrderInfoDTO;
    }

    private CourierOrderInfoDTO formCourierOrderInfoDTO(Order order) {
        List<Container> containers = containerRepository.findAllByOrderId(order.getId());
        List<ContainerComponentsNamesDTO> containersCourierInfoDTOs = containerRepository.findAllByOrderId(order.getId()).stream()
                .map(containerService::mapFromContainerEntityToContainerComponentsNamesDTO).toList();
        CourierOrderInfoDTO courierOrderInfoDTO = new CourierOrderInfoDTO();
        courierOrderInfoDTO.setDate(order.getDate());
        courierOrderInfoDTO.setTime(order.getTime());
        courierOrderInfoDTO.setClientName(order.getClient().getFirstName() + " " + order.getClient().getLastName());
        courierOrderInfoDTO.setPaymentType(order.getPaymentType().toString().toLowerCase(Locale.ROOT));
        courierOrderInfoDTO.setOrderDeliveredOnTime(order.getStatus().equals(OrderStatus.COMPLETED_ON_TIME));
        courierOrderInfoDTO.setOrderCost(containerService.calculateTotalOrderCost(containers));
        courierOrderInfoDTO.setContainers(containersCourierInfoDTOs);
        return courierOrderInfoDTO;
    }

    private CourierPerformanceIndicatorDTO formCourierPerformanceIndicatorDTO(int totalNumberOfOrdersDelivered, int numberOfOrdersDeliveredOnTime,
                                                                              int numberOfOrdersDeliveredLate, double percentageOfOrdersDeliveredOnTime,
                                                                              double numberOfOrdersDeliveredPerDay) {
        CourierPerformanceIndicatorDTO courierPerformanceIndicatorDTO = new CourierPerformanceIndicatorDTO();
        courierPerformanceIndicatorDTO.setTotalNumberOfOrdersDelivered(totalNumberOfOrdersDelivered);
        courierPerformanceIndicatorDTO.setNumberOfOrdersDeliveredOnTime(numberOfOrdersDeliveredOnTime);
        courierPerformanceIndicatorDTO.setNumberOfOrdersDeliveredLate(numberOfOrdersDeliveredLate);
        courierPerformanceIndicatorDTO.setPercentageOfOrdersDeliveredOnTime(percentageOfOrdersDeliveredOnTime);
        courierPerformanceIndicatorDTO.setNumberOfOrdersDeliveredPerDay(numberOfOrdersDeliveredPerDay);
        courierPerformanceIndicatorDTO.setSpeedOfWorkSatisfactory(isSpeedOfWorkSatisfactory(percentageOfOrdersDeliveredOnTime));
        courierPerformanceIndicatorDTO.setDailyAmountOfWorkSatisfactory(isDailyAmountOfWorkSatisfactory(numberOfOrdersDeliveredPerDay));
        return courierPerformanceIndicatorDTO;
    }

    private boolean isSpeedOfWorkSatisfactory(double percentageOfOrdersDeliveredOnTime) {
        return percentageOfOrdersDeliveredOnTime >= normalPercentageOfOrdersDeliveredOnTime;
    }

    private boolean isDailyAmountOfWorkSatisfactory(double numberOfOrdersDeliveredPerDay) {
        return numberOfOrdersDeliveredPerDay >= normOfOrdersPerDay;
    }

    private double calculateNumberOfOrdersDeliveredPerDay(List<CourierOrderInfoDTO> courierOrders) {
        int numberOfWorkingDays = courierOrders.stream().map(CourierOrderInfoDTO::getDate).collect(Collectors.toSet()).size();
        return courierOrders.size() / numberOfWorkingDays;
    }
}
