package edu.senla.service;

import ch.qos.logback.classic.Logger;
import edu.senla.dao.ClientRepositoryInterface;
import edu.senla.dao.ContainerRepositoryInterface;
import edu.senla.dao.CourierRepositoryInterface;
import edu.senla.dao.OrderRepositoryInterface;
import edu.senla.dto.*;
import edu.senla.entity.Client;
import edu.senla.entity.Container;
import edu.senla.entity.Courier;
import edu.senla.entity.Order;
import edu.senla.enums.CRUDOperations;
import edu.senla.enums.CourierStatus;
import edu.senla.enums.OrderStatus;
import edu.senla.exeptions.BadRequest;
import edu.senla.exeptions.ConflictBetweenData;
import edu.senla.exeptions.NotFound;
import edu.senla.service.serviceinterface.ContainerServiceInterface;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import edu.senla.service.serviceinterface.ValidationServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
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
public class CourierService implements CourierServiceInterface{

    private final ContainerServiceInterface containerService;

    private final ValidationServiceInterface validationService;

    private final ContainerRepositoryInterface containerRepository;

    private final OrderRepositoryInterface orderRepository;

    private final ClientRepositoryInterface clientRepository;

    private final CourierRepositoryInterface courierRepository;

    private final PasswordEncoder passwordEncoder;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(ClientService.class);

    private final ModelMapper mapper;

    private final double normOfOrdersPerDay = 4;

    private final double normalPercentageOfOrdersDeliveredOnTime = 75;

    public List<CourierMainInfoDTO> getAllCouriers() {
        LOG.info("Getting all couriers");
        Page<Courier> couriers = courierRepository.findAll(PageRequest.of(0, 10, Sort.by("lastName").descending()));
        return couriers.stream().map(c -> mapper.map(c, CourierMainInfoDTO.class)).toList();
    }

    public List<CourierBasicInfoDTO> getAllActiveCouriersDTO() {
        List<CourierBasicInfoDTO> courierBasicInfoDTOS = getAllActiveCouriers().stream().map(c-> mapper.map(c, CourierBasicInfoDTO.class)).toList();
        if (courierBasicInfoDTOS.isEmpty()) throw new NotFound("Currently there are no active couriers to assign orders");
        return courierBasicInfoDTOS;
    }

    public void createCourier(CourierRegistrationRequestDTO newCourierDTO) {
        LOG.info("A new courier wants to register in the service: " + newCourierDTO);
        checkCourierName(newCourierDTO.getFirstName(), CRUDOperations.CREATE);
        checkCourierName(newCourierDTO.getLastName(), CRUDOperations.CREATE);
        isCourierExistsByPhone(newCourierDTO.getPhone(), CRUDOperations.CREATE);
        checkCourierPhone(newCourierDTO.getPhone(), CRUDOperations.CREATE);
        checkCourierPasswordConfirmation(newCourierDTO);
        newCourierDTO.setPassword(passwordEncoder.encode(newCourierDTO.getPassword()));
        Courier courier = mapper.map(newCourierDTO, Courier.class);
        courier.setStatus(CourierStatus.INACTIVE);
        Courier savedCourier = courierRepository.save(courier);
        LOG.info("A new courier is registered in the service: " + savedCourier);
    }

    public CourierMainInfoDTO getCourier(long id) {
        LOG.info("Getting courier with id {}: ", id);
        Courier courier = getCourierIfExists(id, CRUDOperations.READ);
        CourierMainInfoDTO courierMainInfoDTO = mapper.map(courierRepository.getById(id), CourierMainInfoDTO.class);
        LOG.info("Courier found: {}: ", courierMainInfoDTO);
        return courierMainInfoDTO;
    }

    public CourierBasicInfoDTO getCourierBasicInfo(long id) {
        Courier courier = getCourierIfExists(id, CRUDOperations.READ);
        return mapper.map(courier, CourierBasicInfoDTO.class);
    }

    public CourierCurrentOrderInfoDTO getCurrentOrderForCourier(long id) {
        CourierBasicInfoDTO courierBasicInfo = getCourierBasicInfo(id);
        if (!isCourierActiveNow(id)) {
            LOG.warn("An attempt to get information about the current order for the courier failed, because courier {} {} is inactive now", courierBasicInfo.getFirstName(), courierBasicInfo.getLastName());
            throw new BadRequest("Courier is inactive now");
        }
        CourierCurrentOrderInfoDTO currentOrderInfoDTO = getCourierOrder(id);
        LOG.info("Current order of courier {} {}: {}", courierBasicInfo.getFirstName(), courierBasicInfo.getLastName(), currentOrderInfoDTO);
        return currentOrderInfoDTO;
    }

    public CourierFullInfoDTO getCourierByPhoneAndPassword(String phone, String password) {
        try {
            Courier courier = courierRepository.getByPhone(phone);
            if(!passwordEncoder.matches(password, courier.getPassword())) throw new BadRequest();
            return mapper.map(courier, CourierFullInfoDTO.class);
        } catch (RuntimeException exception) {
            LOG.error("No courier found with phone {} and password {}", phone, password);
            throw new NotFound("Invalid phone or password");
        }
    }

    public void updateCourier(long id, CourierMainInfoDTO courierDTO) {
        Courier courierToUpdate = getCourierIfExists(id, CRUDOperations.UPDATE);
        LOG.info("Updating courier with id {} with new data {}: ", id, courierDTO);
        checkCourierName(courierDTO.getFirstName(), CRUDOperations.UPDATE);
        checkCourierName(courierDTO.getLastName(), CRUDOperations.UPDATE);
        checkCourierPhone(courierDTO.getPhone(), CRUDOperations.UPDATE);
        isCourierExistsByPhone(courierDTO.getPhone(), CRUDOperations.UPDATE);
        Courier updatedCourier = mapper.map(courierDTO, Courier.class);
        Courier courierWithNewParameters = updateCouriersOptions(courierToUpdate, updatedCourier);
        courierRepository.save(courierWithNewParameters);
        LOG.info("Courier with id {} successfully updated", id);
    }

    public void deleteCourier(long id) {
        LOG.info("Deleting courier with id: {}", id);
        Courier courierToDelete = getCourierIfExists(id, CRUDOperations.DELETE);
        courierRepository.deleteById(id);
        LOG.info("Courier with id {} successfully deleted", id);
    }

    public long getCurrentCourierId() {
        String courierPhone = getCurrentCourierPhone();
        return courierRepository.getByPhone(courierPhone).getId();
    }

    private Courier getCourierIfExists(long id, CRUDOperations operation){
        if (!courierRepository.existsById(id)) {
            LOG.info("The attempt to {} a courier failed, there is no courier with id {}", operation.toString().toLowerCase(), id);
            throw new NotFound("There is no courier with id " + id);
        }
        return courierRepository.getById(id);
    }

    private void isCourierExistsByPhone(String phone, CRUDOperations operation) {
        if (courierRepository.getByPhone(phone) != null){
            LOG.info("The attempt to {} a courier failed, because courier with phone {} already exists", operation.toString().toLowerCase(Locale.ROOT), phone);
            throw new ConflictBetweenData("A courier with this " + phone + " already exists ");
        }
    }

    public void changeCourierStatus(long id) {
        Courier courier = courierRepository.getById(id);
        CourierStatus newStatus = courier.getStatus().equals(CourierStatus.INACTIVE) ? CourierStatus.ACTIVE : CourierStatus.INACTIVE;
        courier.setStatus(newStatus);
        LOG.info("{} {} changed his/her status to {}", courier.getFirstName(), courier.getLastName(), newStatus.toString().toLowerCase(Locale.ROOT));
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
        LOG.info("{} orders assigned to couriers, {} pending", numberOfAssignedOrders, courierBasicInfoDTOS.size() - numberOfAssignedOrders);
    }

    public void assignNewOrdersToCourier(long id) {
        Courier courier = courierRepository.getById(id);
        if (!isCourierActiveNow(id)) {
            LOG.info("The courier's {} {} attempt to receive a new order failed because this courier is inactive now", courier.getFirstName(), courier.getLastName());
            throw new BadRequest("You are not active right now. Please update your working status to receive new orders.");
        }
        List<Order> newOrders = getAllNewOrders();
        if (isCourierOccupied(courier)){
            LOG.info("The courier's {} {} attempt to receive a new order failed because courier already has an order", courier.getFirstName(), courier.getLastName());
            throw new ConflictBetweenData("You already has an order");
        }
        if (newOrders.isEmpty() || isCourierOccupied(courier)){
            LOG.info("The courier's {} {} attempt to receive a new order failed because there are no available orders right now", courier.getFirstName(), courier.getLastName());
            throw new NotFound("There are no available orders right now");
        }
        Order earliestOrder = newOrders.get(0);
        setOrderToCourier(courier, earliestOrder);
        LOG.info("Order with {} assigned to courier {} {}", earliestOrder.getId(), courier.getFirstName(), courier.getLastName());
    }

    public List<CourierOrderInfoDTO> getAllOrdersOfCourier(long courierId) {
        Courier courier = getCourierIfExists(courierId, CRUDOperations.READ);
        LOG.info("Requested order history for the courier {} {}", courier.getFirstName(), courier.getLastName());
        return orderRepository.getAllByCourier(courier, PageRequest.of(0, 10, Sort.by("date").descending())).stream()
                .filter(o -> o.getStatus().equals(OrderStatus.COMPLETED_LATE) || o.getStatus().equals(OrderStatus.COMPLETED_ON_TIME))
                .map(o -> formCourierOrderInfoDTO(o))
                .toList();
    }

    private boolean isCourierActiveNow(long id) {
        return courierRepository.getById(id).getStatus().equals(CourierStatus.ACTIVE);
    }

    private CourierPerformanceIndicatorDTO calculateCourierPerformanceIndicator(Courier courier) {
        List<CourierOrderInfoDTO> courierOrders = getAllOrdersOfCourier(courier.getId());
        if (courierOrders.isEmpty()) return null;
        int totalNumberOfOrdersDelivered = courierOrders.size();
        int numberOfOrdersDeliveredOnTime = (int) courierOrders.stream().filter(o -> o.isOrderDeliveredOnTime()).count();
        int numberOfOrdersDeliveredLate = totalNumberOfOrdersDelivered - numberOfOrdersDeliveredOnTime;
        double percentageOfOrdersDeliveredOnTime = ((double)numberOfOrdersDeliveredOnTime/totalNumberOfOrdersDelivered) * 100;
        double numberOfOrdersDeliveredPerDay = calculateNumberOfOrdersDeliveredPerDay(courierOrders);
        return formCourierPerformanceIndicatorDTO(totalNumberOfOrdersDelivered, numberOfOrdersDeliveredOnTime,
                numberOfOrdersDeliveredLate, percentageOfOrdersDeliveredOnTime, numberOfOrdersDeliveredPerDay);
    }

    public CourierPerformanceIndicatorDTO getCourierPerformanceIndicator(long id) {
        Courier courier = getCourierIfExists(id, CRUDOperations.READ);
        LOG.info("Requested performance indicator of the courier {} {}", courier.getFirstName(), courier.getLastName());
        CourierPerformanceIndicatorDTO courierPerformanceIndicatorDTO = calculateCourierPerformanceIndicator(courier);
        if (courierPerformanceIndicatorDTO == null){
            LOG.info("The attempt to get the courier's performance indicator failed because there is no closed orders found for courier {} {}", courier.getFirstName(), courier.getLastName());
            throw new NotFound("No closed orders found for courier to form courier performance indicator");
        }
        return courierPerformanceIndicatorDTO;
    }

    private CourierCurrentOrderInfoDTO getCourierOrder(long id) {
        Courier courier = courierRepository.getById(id);
        Order order = orderRepository.getByCourierAndStatus(courier, OrderStatus.IN_PROCESS);
        if (order == null) {
            LOG.warn("An attempt to get information about the current order for the courier failed, because, courier {} {} has no current order", courier.getFirstName(), courier.getLastName());
            throw new NotFound("Courier has no current order");
        }
        Client client = clientRepository.getById(order.getClient().getId());
        List<Container> containers = containerRepository.findAllByOrderId(order.getId());
        double orderCost = containerService.calculateTotalOrderCost(containers);
        List<ContainerComponentsNamesDTO> containersCourierInfoDTOs = containers.stream()
                .map(containerService::mapFromContainerEntityToContainerComponentsNamesDTO).toList();
        return formCourierOrderInfoResponseDTO(containersCourierInfoDTOs, client, order, orderCost);
    }

    private void checkCourierName(String name, CRUDOperations operation){
        if (!validationService.isNameCorrect(name)) {
            LOG.error("The attempt to {} a courier failed, a courier name {} contains invalid characters", operation.toString().toLowerCase(), name);
            throw new BadRequest("Name " + name + " contains invalid characters");
        }
        if (!validationService.isNameLengthValid(name)) {
            LOG.error("The attempt to {} a courier failed, a courier name {} is to short", operation.toString().toLowerCase(), name);
            throw new BadRequest("Name " + name + " is too short");
        }
    }

    private void checkCourierPhone(String phone, CRUDOperations operation){
        if (!validationService.isPhoneCorrect(phone)) {
            LOG.error("The attempt to {} a courier failed, a client phone {} is invalid", operation.toString().toLowerCase(), phone);
            throw new BadRequest("Phone " + phone + " is invalid");
        }
    }

    private void checkCourierPasswordConfirmation(CourierRegistrationRequestDTO courierRegistrationRequestDTO){
        if (!(courierRegistrationRequestDTO.getPassword()).equals(courierRegistrationRequestDTO.getPasswordConfirm())) {
            LOG.info("The attempt to register a courier failed, because passwords {} and {} do not match",
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

    private boolean isCourierOccupied(Courier courier){
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
                                                                       Client client, Order order, double orderCost){
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
        int numberOfWorkingDays = courierOrders.stream().map(o -> o.getDate()).collect(Collectors.toSet()).size();
        return courierOrders.size()/numberOfWorkingDays;
    }

}
