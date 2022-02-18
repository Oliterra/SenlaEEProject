package edu.senla.service.impl;

import edu.senla.dao.ClientRepository;
import edu.senla.dao.ContainerRepository;
import edu.senla.dao.CourierRepository;
import edu.senla.dao.OrderRepository;
import edu.senla.exeption.BadRequest;
import edu.senla.exeption.NotFound;
import edu.senla.model.dto.*;
import edu.senla.model.entity.Client;
import edu.senla.model.entity.Container;
import edu.senla.model.entity.Courier;
import edu.senla.model.entity.Order;
import edu.senla.model.enums.OrderPaymentType;
import edu.senla.model.enums.OrderStatus;
import edu.senla.service.ContainerService;
import edu.senla.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class OrderServiceImpl extends AbstractService implements OrderService {

    private final ContainerService containerService;
    private final OrderRepository orderRepository;
    private final ContainerRepository containerRepository;
    private final ClientRepository clientRepository;
    private final CourierRepository courierRepository;
    private static final int deliveryTimeStandard = 120;

    public List<OrderDTO> getAllOrders(int pages) {
        log.info("Getting all orders");
        Page<Order> orders = orderRepository.findAll(PageRequest.of(0, pages, Sort.by("date").descending()));
        return orders.stream().map(o -> modelMapper.map(o, OrderDTO.class)).toList();
    }

    @SneakyThrows
    public OrderTotalCostDTO checkIncomingOrderDataAndCreateIfItIsCorrect(long clientId, String shoppingCartJson) {
        ShoppingCartDTO shoppingCartDTO = objectMapper.readValue(shoppingCartJson, ShoppingCartDTO.class);
        List<ContainerComponentsDTO> correctContainers = containerService.filterContainers(shoppingCartDTO.getContainers());
        if (correctContainers.isEmpty()) {
            log.error("Attempt to place an order failed, there is no items in shopping cart");
            throw new BadRequest("There is no items in shopping cart");
        }
        shoppingCartDTO.setContainers(correctContainers);
        Client client = clientRepository.getById(clientId);
        OrderTotalCostDTO orderTotalCostDTO = createNewOrder(client, shoppingCartDTO);
        client.setAddress(shoppingCartDTO.getAddress());
        return orderTotalCostDTO;
    }

    public OrderDTO getOrder(long id) {
        log.info("Getting order with id {}: ", id);
        Order order = getOrderIfExists(id);
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        orderDTO.setStatus(order.getStatus().toString().toLowerCase());
        orderDTO.setPaymentType(order.getPaymentType().toString().toLowerCase());
        log.info("Order found: {}: ", orderDTO);
        return orderDTO;
    }

    public void deleteOrder(long id) {
        log.info("Deleting order with id: {}", id);
        checkOrderExistence(id);
        orderRepository.deleteById(id);
        log.info("Order with id {} successfully deleted", id);
    }

    public void closeOrderForClient(long clientId, long orderId) {
        if (!isOrderBelongToClient(orderId, clientId)) {
            log.info("The order {} does not belong to the client {}", orderId, clientId);
            throw new BadRequest("This order does not belong to you");
        }
        if (!isOrderIsInProcess(orderId)) {
            log.info("The order {} cannot be closed by the client, incorrect status", orderId);
            throw new BadRequest("The order cannot be closed because of its incorrect status");
        }
        Order order = orderRepository.getById(orderId);
        order.setStatus(OrderStatus.RECEIPT_CONFIRMED);
        orderRepository.save(order);
        log.info("The order receipt {} is confirmed", orderId);
    }

    public OrderClosingResponseDTO closeOrderForCourier(long id) {
        Courier courier = courierRepository.getById(id);
        OrderStatusInfoDTO orderStatusInfoDTO = getOrderStatusInfo(id);
        if (!isOrderConfirmedByClient(orderStatusInfoDTO.getId())) {
            log.info("The attempt to close the order by courier {} {} failed because, the user has not confirmed its receipt", courier.getFirstName(), courier.getLastName());
            throw new BadRequest("You cannot close the order because the courier has not confirmed its receipt");
        }
        Order order = orderRepository.getByCourierAndStatus(courier, OrderStatus.RECEIPT_CONFIRMED);
        long executionTime = ChronoUnit.MINUTES.between(order.getTime(), LocalTime.now());
        closeOrder(order, executionTime < deliveryTimeStandard);
        OrderClosingResponseDTO orderClosingResponseDTO = formOrderClosingResponseDTO(executionTime);
        log.info("Courier {} {} closed order with id {} in time {}", courier.getFirstName(), courier.getLastName(), orderStatusInfoDTO.getId(), orderClosingResponseDTO.getExecutionTime());
        return orderClosingResponseDTO;
    }

    private OrderTotalCostDTO createNewOrder(Client client, ShoppingCartDTO shoppingCartDTO) {
        Order order = new Order();
        order.setClient(client);
        order.setStatus(OrderStatus.NEW);
        order.setPaymentType(translateOrderPaymentType(shoppingCartDTO.getPaymentType()));
        order.setDate(LocalDate.now());
        order.setTime(LocalTime.now());
        orderRepository.saveAndFlush(order);
        List<Container> containers = shoppingCartDTO.getContainers().stream()
                .map(container -> containerService.mapFromContainerComponentsDTOToContainerEntity(container, order))
                .toList();
        List<Container> createdContainers = containerRepository.saveAll(containers);
        return createOrderTotalCostDTO(createdContainers);
    }

    private Order getOrderIfExists(long id) {
        if (!orderRepository.existsById(id)) {
            log.info("The attempt to get a order failed, there is no order with id {}", id);
            throw new NotFound("There is no order with id " + id);
        }
        return orderRepository.getById(id);
    }

    private void checkOrderExistence(long id) {
        if (!orderRepository.existsById(id)) {
            log.info("The attempt to delete a order failed, there is no order with id {}", id);
            throw new NotFound("There is no order with id " + id);
        }
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

    private OrderStatusInfoDTO formOrderStatusInfoDTO(Optional<Order> order) {
        return order.map(item -> new OrderStatusInfoDTO()
                .setStatus(item.getStatus().toString().toLowerCase(Locale.ROOT))
                .setId(item.getId()))
                .orElse(null);
    }

    private OrderTotalCostDTO createOrderTotalCostDTO(List<Container> containers) {
        OrderTotalCostDTO orderTotalCostDTO = new OrderTotalCostDTO();
        orderTotalCostDTO.setOrderTotalCost(containerService.calculateTotalOrderCost(containers));
        List<ContainerComponentsNamesDTO> containerComponentsDTOS = containers.stream().map(containerService::mapFromContainerEntityToContainerComponentsNamesDTO).toList();
        orderTotalCostDTO.setContainerComponentsDTOS(containerComponentsDTOS);
        return orderTotalCostDTO;
    }

    private OrderClosingResponseDTO formOrderClosingResponseDTO(long executionTime) {
        OrderClosingResponseDTO closingResponseDTO = new OrderClosingResponseDTO();
        closingResponseDTO.setExecutionTime(executionTime);
        closingResponseDTO.setOrderDeliveredOnTime(executionTime < deliveryTimeStandard);
        return closingResponseDTO;
    }

    private void closeOrder(Order order, boolean isOrderCompletedOnTime) {
        OrderStatus orderStatus = isOrderCompletedOnTime ? OrderStatus.COMPLETED_ON_TIME : OrderStatus.COMPLETED_LATE;
        order.setStatus(orderStatus);
        orderRepository.save(order);
    }

    private OrderPaymentType translateOrderPaymentType(String orderPaymentType) {
        return switch (orderPaymentType) {
            case "by card online" -> OrderPaymentType.BY_CARD_ONLINE;
            case "by card to courier" -> OrderPaymentType.BY_CARD_TO_COURIER;
            case "cash to courier" -> OrderPaymentType.CASH_TO_COURIER;
            default -> {
                log.error("The attempt to create an order failed, a order payment type {} invalid", orderPaymentType);
                throw new BadRequest("Order payment type " + orderPaymentType + " is invalid");
            }
        };
    }
}
