package edu.senla.service;

import edu.senla.dao.ClientRepositoryInterface;
import edu.senla.dao.ContainerRepositoryInterface;
import edu.senla.dao.CourierRepositoryInterface;
import edu.senla.dao.OrderRepositoryInterface;
import edu.senla.dto.*;
import edu.senla.entity.Client;
import edu.senla.entity.Container;
import edu.senla.entity.Courier;
import edu.senla.entity.Order;
import edu.senla.service.serviceinterface.ContainerServiceInterface;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class CourierService implements CourierServiceInterface{

    private final ContainerServiceInterface containerService;

    private final ContainerRepositoryInterface containerRepository;

    private final OrderRepositoryInterface orderRepository;

    private final ClientRepositoryInterface clientRepository;

    private final CourierRepositoryInterface courierRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper mapper;

    private final double normOfOrdersPerDay = 4;

    private final double normalPercentageOfOrdersDeliveredOnTime = 75;

    public List<CourierMainInfoDTO> getAllCouriers() {
        Page<Courier> couriers = courierRepository.findAll(PageRequest.of(0, 10, Sort.by("lastName").descending()));
        return couriers.stream().map(c -> mapper.map(c, CourierMainInfoDTO.class)).toList();
    }

    public List<CourierBasicInfoDTO> getAllActiveCouriersDTO() {
        return getAllActiveCouriers().stream().map(c-> mapper.map(c, CourierBasicInfoDTO.class)).toList();
    }

    public void createCourier(CourierRegistrationRequestDTO newCourierDTO) {
        newCourierDTO.setPassword(passwordEncoder.encode(newCourierDTO.getPassword()));
        Courier courier = mapper.map(newCourierDTO, Courier.class);
        courier.setStatus("inactive");
        courierRepository.save(courier);
    }

    public CourierMainInfoDTO getCourier(long id) {
        try {
            return mapper.map(courierRepository.getById(id), CourierMainInfoDTO.class);
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public CourierBasicInfoDTO getCourierBasicInfo(long id) {
        try {
            return mapper.map(courierRepository.getById(id), CourierBasicInfoDTO.class);
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public CourierCurrentOrderInfoDTO getCurrentOrderForCourier(long id) {
        Courier courier = courierRepository.getById(id);
        Order order = orderRepository.getByCourierAndStatus(courier, "in process");
        if (order == null) return null;
        Client client = clientRepository.getById(order.getClient().getId());
        List<Container> containers = containerRepository.findAllByOrderId(order.getId());
        double orderCost = containerService.calculateTotalOrderCost(containers);
        List<ContainerComponentsNamesDTO> containersCourierInfoDTOs = containers.stream()
                .map(containerService::mapFromContainerEntityToContainerComponentsNamesDTO).toList();
        return formCourierOrderInfoResponseDTO(containersCourierInfoDTOs, client, order, orderCost);
    }

    public CourierFullInfoDTO getCourierByPhoneAndPassword(String phone, String password) {
        Courier courier = courierRepository.getByPhone(phone);
        if (courier == null) return null;
        CourierFullInfoDTO courierFullInfoDTO = mapper.map(courier, CourierFullInfoDTO.class);
        return passwordEncoder.matches(password, courier.getPassword()) ? courierFullInfoDTO : null;
    }

    public void updateCourier(long id, CourierMainInfoDTO courierDTO) {
        Courier updatedCourier = mapper.map(courierDTO, Courier.class);
        Courier courierToUpdate = courierRepository.getById(id);
        Courier courierWithNewParameters = updateCouriersOptions(courierToUpdate, updatedCourier);
        courierRepository.save(courierWithNewParameters);
    }

    public void deleteCourier(long id) {
        courierRepository.deleteById(id);
    }

    public long getCurrentCourierId() {
        String courierPhone = getCurrentCourierPhone();
        return courierRepository.getByPhone(courierPhone).getId();
    }

    public boolean isCourierExists(String phone) {
        return courierRepository.getByPhone(phone) != null;
    }

    public boolean isCourierExists(long id) {
        return courierRepository.existsById(id);
    }

    public boolean isCourierActiveNow(long id) {
        return courierRepository.getById(id).getStatus().equals("active");
    }

    public String changeCourierStatus(long id) {
        Courier courier = courierRepository.getById(id);
        String newStatus = courier.getStatus().equals("inactive") ? "active" : "inactive";
        courier.setStatus(newStatus);
        courierRepository.save(courier);
        return newStatus;
    }

    public int assignOrdersToAllActiveCouriers(List<CourierBasicInfoDTO> courierBasicInfoDTOS) {
        List<Order> orders = getAllNewOrders();
        List<Courier> couriers = getAllUnoccupiedCouriers();
        if (orders.size() > couriers.size()) {
            for (int i = 0; i < couriers.size(); i++) {
                setOrderToCourier(couriers.get(i), orders.get(i));
            }
            return couriers.size();
        } else {
            for (int i = 0; i < orders.size(); i++) {
                setOrderToCourier(couriers.get(i), orders.get(i));
            }
            return orders.size();
        }
    }

    public long assignNewOrdersToCourier(long id) {
        Courier courier = courierRepository.getById(id);
        List<Order> newOrders = getAllNewOrders();
        if (newOrders.isEmpty() || isCourierOccupied(courier)) return 0;
        Order earliestOrder = newOrders.get(0);
        setOrderToCourier(courier, earliestOrder);
        return earliestOrder.getId();
    }

    public List<CourierOrderInfoDTO> getAllOrdersOfCourier(long courierId) {
        Courier courier = courierRepository.getById(courierId);
        return orderRepository.getAllByCourier(courier, PageRequest.of(0, 10, Sort.by("date").descending())).stream()
                .filter(o -> o.getStatus().equals("completed late") || o.getStatus().equals("completed on time"))
                .map(o -> formCourierOrderInfoDTO(o))
                .toList();
    }

    public CourierPerformanceIndicatorDTO calculateCourierPerformanceIndicator(long id) {
        Courier courier = courierRepository.getById(id);
        List<CourierOrderInfoDTO> courierOrders = getAllOrdersOfCourier(id);
        if (courierOrders.isEmpty()) return null;
        int totalNumberOfOrdersDelivered = courierOrders.size();
        int numberOfOrdersDeliveredOnTime = (int) courierOrders.stream().filter(o -> o.isOrderDeliveredOnTime()).count();
        int numberOfOrdersDeliveredLate = totalNumberOfOrdersDelivered - numberOfOrdersDeliveredOnTime;
        double percentageOfOrdersDeliveredOnTime = ((double)numberOfOrdersDeliveredOnTime/totalNumberOfOrdersDelivered) * 100;
        double numberOfOrdersDeliveredPerDay = calculateNumberOfOrdersDeliveredPerDay(courierOrders);
        return formCourierPerformanceIndicatorDTO(totalNumberOfOrdersDelivered, numberOfOrdersDeliveredOnTime,
                numberOfOrdersDeliveredLate, percentageOfOrdersDeliveredOnTime, numberOfOrdersDeliveredPerDay);
    }

    private List<Order> getAllNewOrders() {
        return orderRepository.getByStatusOrderByTimeAsc("new");
    }

    private List<Courier> getAllActiveCouriers() {
        return courierRepository.getByStatus("active", PageRequest.of(0, 10, Sort.by("lastName").descending()));
    }

    private List<Courier> getAllUnoccupiedCouriers() {
        return getAllActiveCouriers().stream().filter(c -> !isCourierOccupied(c)).toList();
    }

    private boolean isCourierOccupied(Courier courier){
        List<Courier> clientsWitOpenOrders = orderRepository.getAllCouriersByStatus("in process");
        List<Courier> clientsWithClosedOrders = orderRepository.getAllCouriersByStatus("receipt confirmed");
        return clientsWitOpenOrders.contains(courier) || clientsWithClosedOrders.contains(courier);
    }

    private void setOrderToCourier(Courier courier, Order order) {
        order.setCourier(courier);
        order.setStatus("in process");
    }

    private Courier updateCouriersOptions(Courier courier, Courier updatedCourier) {
        courier.setFirstName(updatedCourier.getFirstName());
        courier.setLastName(updatedCourier.getLastName());
        courier.setPhone(updatedCourier.getPhone());
        courier.setStatus(updatedCourier.getStatus());
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
        courierCurrentOrderInfoDTO.setPaymentType(order.getPaymentType());
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
        courierOrderInfoDTO.setPaymentType(order.getPaymentType());
        courierOrderInfoDTO.setOrderDeliveredOnTime(order.getStatus().equals("completed on time"));
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
