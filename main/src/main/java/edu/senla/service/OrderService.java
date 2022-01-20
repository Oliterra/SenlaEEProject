package edu.senla.service;

import ch.qos.logback.classic.Logger;
import edu.senla.dao.ClientRepositoryInterface;
import edu.senla.dao.ContainerRepositoryInterface;
import edu.senla.dao.CourierRepositoryInterface;
import edu.senla.dao.OrderRepositoryInterface;
import edu.senla.dto.*;
import edu.senla.entity.*;
import edu.senla.enums.CRUDOperations;
import edu.senla.enums.OrderPaymentType;
import edu.senla.enums.OrderStatus;
import edu.senla.exeptions.BadRequest;
import edu.senla.exeptions.NotFound;
import edu.senla.service.serviceinterface.ContainerServiceInterface;
import edu.senla.service.serviceinterface.OrderServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderService implements OrderServiceInterface {

    private final ContainerServiceInterface containerService;

    private final OrderRepositoryInterface orderRepository;

    private final ContainerRepositoryInterface containerRepository;

    private final ClientRepositoryInterface clientRepository;

    private final CourierRepositoryInterface courierRepository;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(OrderService.class);

    private final ModelMapper mapper;

    private final int deliveryTimeStandard = 120;

    public List<OrderDTO> getAllOrders() {
        LOG.info("Getting all orders");
        Page<Order> orders = orderRepository.findAll(PageRequest.of(0, 10, Sort.by("date").descending()));
        return orders.stream().map(o -> mapper.map(o, OrderDTO.class)).toList();
    }

    public OrderTotalCostDTO checkIncomingOrderDataAndCreateIfItIsCorrect(long clientId, ShoppingCartDTO shoppingCartDTO){
        List correctContainers = containerService.filterContainers(shoppingCartDTO.getContainers());
        if (correctContainers.isEmpty()){
            LOG.error("Attempt to place an order failed, there is no items in shopping cart");
            throw new BadRequest("There is no items in shopping cart");
        }
        shoppingCartDTO.setContainers(correctContainers);
        Client client = clientRepository.getById(clientId);
        OrderTotalCostDTO orderTotalCostDTO = createNewOrder(client, shoppingCartDTO);
        client.setAddress(shoppingCartDTO.getAddress());
        return orderTotalCostDTO;
    }

    public OrderDTO getOrder(long id) {
        LOG.info("Getting order with id {}: ", id);
        Order order = getOrderIfExists(id, CRUDOperations.READ);
        OrderDTO orderDTO = mapper.map(order, OrderDTO.class);
        orderDTO.setStatus(order.getStatus().toString().toLowerCase());
        orderDTO.setPaymentType(order.getPaymentType().toString().toLowerCase());
        LOG.info("Order found: {}: ", orderDTO);
        return orderDTO;
    }

    public void deleteOrder(long id) {
        LOG.info("Deleting order with id: {}", id);
        Order orderToDelete = getOrderIfExists(id, CRUDOperations.DELETE);
        orderRepository.deleteById(id);
        LOG.info("Order with id {} successfully deleted", id);
    }

    public void closeOrderForClient(long clientId, long orderId) {
        if (!isOrderBelongToClient(orderId, clientId)) {
            LOG.info("The order {} does not belong to the client {}", orderId, clientId);
            throw new BadRequest("This order does not belong to you");
        }
        if (!isOrderIsInProcess(orderId)) {
            LOG.info("The order {} cannot be closed by the client, incorrect status", orderId);
            throw new BadRequest("The order cannot be closed because of its incorrect status");
        }
        Order order = orderRepository.getById(orderId);
        order.setStatus(OrderStatus.RECEIPT_CONFIRMED);
        orderRepository.save(order);
        LOG.info("The order receipt {} is confirmed", orderId);
    }

    public OrderClosingResponseDTO closeOrderForCourier(long id) {
        Courier courier = courierRepository.getById(id);
        OrderStatusInfoDTO orderStatusInfoDTO = getOrderStatusInfo(id);
        if (!isOrderConfirmedByClient(orderStatusInfoDTO.getId())) {
            LOG.info("The attempt to close the order by courier {} {} failed because, the user has not confirmed its receipt", courier.getFirstName(), courier.getLastName());
            throw new BadRequest("You cannot close the order because the courier has not confirmed its receipt");
        }
        Order order = orderRepository.getByCourierAndStatus(courier, OrderStatus.RECEIPT_CONFIRMED);
        long executionTime = ChronoUnit.MINUTES.between(order.getTime(), LocalTime.now());
        closeOrder(order, executionTime < deliveryTimeStandard);
        OrderClosingResponseDTO orderClosingResponseDTO = formOrderClosingResponseDTO(executionTime);
        LOG.info("Courier {} {} closed order with id {} in time {}", courier.getFirstName(), courier.getLastName(), orderStatusInfoDTO.getId(), orderClosingResponseDTO.getExecutionTime());
        return orderClosingResponseDTO;
    }

    private OrderTotalCostDTO createNewOrder(Client client, ShoppingCartDTO shoppingCartDTO) {
        Order order = new Order();
        order.setClient(client);
        order.setStatus(OrderStatus.NEW);
        order.setPaymentType(translateOrderPaymentType(shoppingCartDTO.getPaymentType(), CRUDOperations.CREATE));
        order.setDate(LocalDate.now());
        order.setTime(LocalTime.now());
        Order createdOrder = orderRepository.saveAndFlush(order);
        List<Container> containers = shoppingCartDTO.getContainers().stream()
                .map(container -> containerService.mapFromContainerComponentsDTOToContainerEntity(container, order))
                .toList();
        List<Container> createdContainers = containerRepository.saveAll(containers);
        return createOrderTotalCostDTO(createdOrder, createdContainers);
    }

    private Order getOrderIfExists(long id, CRUDOperations operation){
        if (!orderRepository.existsById(id)) {
            LOG.info("The attempt to {} a order failed, there is no order with id {}", operation.toString().toLowerCase(), id);
            throw new NotFound("There is no order with id " + id);
        }
        return orderRepository.getById(id);
    }

    private OrderStatusInfoDTO getOrderStatusInfo(long courierId) {
        Courier courier = courierRepository.getById(courierId);
        Optional<Order> order = orderRepository.getAllByCourier(courier, PageRequest.of(0, 1, Sort.by("date").descending())).stream()
                .filter(o -> o.getStatus().equals(OrderStatus.RECEIPT_CONFIRMED) || o.getStatus().equals(OrderStatus.IN_PROCESS)).findFirst();
        return formOrderStatusInfoDTO(order);
    }

    private boolean isOrderIsInProcess(long id) {
        return orderRepository.getById(id).getStatus().equals(OrderStatus.IN_PROCESS);
    }

    private boolean isOrderConfirmedByClient(long id) {
        return orderRepository.getById(id).getStatus().equals(OrderStatus.RECEIPT_CONFIRMED);
    }

    private boolean isOrderBelongToClient(long id, long clientId) {
        Order order = orderRepository.getById(id);
        Client client = clientRepository.getById(clientId);
        return order.getClient().equals(client);
    }

    private OrderStatusInfoDTO formOrderStatusInfoDTO(Optional<Order> order){
        if (order.isPresent()) {
            OrderStatusInfoDTO orderStatusInfoDTO = new OrderStatusInfoDTO();
            orderStatusInfoDTO.setStatus(order.get().getStatus().toString().toLowerCase(Locale.ROOT));
            orderStatusInfoDTO.setId(order.get().getId());
            return orderStatusInfoDTO;
        } else return null;
    }

    private OrderTotalCostDTO createOrderTotalCostDTO(Order order, List<Container> containers){
        OrderTotalCostDTO orderTotalCostDTO = new OrderTotalCostDTO();
        orderTotalCostDTO.setOrderTotalCost(containerService.calculateTotalOrderCost(containers));
        List<ContainerComponentsNamesDTO> containerComponentsDTOS = containers.stream().map(containerService::mapFromContainerEntityToContainerComponentsNamesDTO).toList();
        orderTotalCostDTO.setContainerComponentsDTOS(containerComponentsDTOS);
        return orderTotalCostDTO;
    }

    private OrderClosingResponseDTO formOrderClosingResponseDTO(long executionTime){
        OrderClosingResponseDTO closingResponseDTO = new OrderClosingResponseDTO();
        closingResponseDTO.setExecutionTime(executionTime);
        closingResponseDTO.setOrderDeliveredOnTime(executionTime < deliveryTimeStandard);
        return closingResponseDTO;
    }

    private void closeOrder(Order order, boolean isOrderCompletedOnTime){
        OrderStatus orderStatus = isOrderCompletedOnTime ? OrderStatus.COMPLETED_ON_TIME : OrderStatus.COMPLETED_LATE;
        order.setStatus(orderStatus);
        orderRepository.save(order);
    }

    private OrderPaymentType translateOrderPaymentType(String orderPaymentType, CRUDOperations operation) {
        return switch (orderPaymentType) {
            case "by card online" -> OrderPaymentType.BY_CARD_ONLINE;
            case "by card to courier" -> OrderPaymentType.BY_CARD_TO_COURIER;
            case "cash to courier" -> OrderPaymentType.CASH_TO_COURIER;
            default -> {
                LOG.error("The attempt to {} an order failed, a order payment type {} invalid", operation.toString().toLowerCase(), orderPaymentType);
                throw new BadRequest("Order payment type " + orderPaymentType + " is invalid");
            }
        };
    }

}
