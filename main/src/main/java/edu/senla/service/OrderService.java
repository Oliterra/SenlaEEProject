package edu.senla.service;

import edu.senla.dao.ClientRepositoryInterface;
import edu.senla.dao.CourierRepositoryInterface;
import edu.senla.dao.OrderRepositoryInterface;
import edu.senla.dto.OrderClosingResponseDTO;
import edu.senla.dto.OrderDTO;
import edu.senla.dto.OrderForUpdateDTO;
import edu.senla.dto.OrderStatusInfoDTO;
import edu.senla.entity.Client;
import edu.senla.entity.Courier;
import edu.senla.entity.Order;
import edu.senla.service.serviceinterface.OrderServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderService implements OrderServiceInterface {

    private final OrderRepositoryInterface orderRepository;

    private final ClientRepositoryInterface clientRepository;

    private final CourierRepositoryInterface courierRepository;

    private final ModelMapper mapper;

    private final int deliveryTimeStandard = 120;

    public List<OrderDTO> getAllOrders() {
        Page<Order> orders = orderRepository.findAll(PageRequest.of(0, 10, Sort.by("date").descending()));
        return orders.stream().map(o -> mapper.map(o, OrderDTO.class)).toList();
    }

    public OrderDTO getOrder(long id) {
        try {
            return mapper.map(orderRepository.getById(id), OrderDTO.class);
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public void updateOrder(long id, OrderForUpdateDTO orderDTO) {
        Order updatedOrder = mapper.map(orderDTO, Order.class);
        Order orderToUpdate = orderRepository.getById(id);
        Order orderWithNewParameters = updateOrdersOptions(orderToUpdate, updatedOrder);
        orderRepository.save(orderWithNewParameters);
    }

    public void deleteOrder(long id) {
        orderRepository.deleteById(id);
    }

    public OrderStatusInfoDTO getOrderStatusInfo(long courierId) {
        Courier courier = courierRepository.getById(courierId);
        Optional<Order> order = orderRepository.getAllByCourier(courier, PageRequest.of(0, 1, Sort.by("date").descending())).stream()
                .filter(o -> o.getStatus().equals("receipt confirmed") || o.getStatus().equals("in process")).findFirst();
        return formOrderStatusInfoDTO(order);
    }

    public boolean isOrderIsInProcess(long id) {
        return orderRepository.getById(id).getStatus().equals("in process");
    }

    public boolean isOrderConfirmedByClient(long id) {
        return orderRepository.getById(id).getStatus().equals("receipt confirmed");
    }

    public void closeOrderForClient(long id) {
        Order order = orderRepository.getById(id);
        order.setStatus("receipt confirmed");
        orderRepository.save(order);
    }

    public boolean isOrderBelongToClient(long id, long clientId) {
        Order order = orderRepository.getById(id);
        Client client = clientRepository.getById(clientId);
        return order.getClient().equals(client);
    }

    public boolean isOrderExists(long id) {
        return orderRepository.existsById(id);
    }

    public OrderClosingResponseDTO closeOrderForCourier(long courierId) {
        Courier courier = courierRepository.getById(courierId);
        Order order = orderRepository.getByCourierAndStatus(courier, "receipt confirmed");
        long executionTime = ChronoUnit.MINUTES.between(order.getTime(), LocalTime.now());
        closeOrder(order, executionTime < deliveryTimeStandard);
        return formOrderClosingResponseDTO(executionTime);
    }

    private Order updateOrdersOptions(Order order, Order updatedOrder) {
        order.setDate(updatedOrder.getDate());
        order.setTime(updatedOrder.getTime());
        order.setPaymentType(updatedOrder.getPaymentType());
        order.setStatus(updatedOrder.getStatus());
        return order;
    }

    private OrderStatusInfoDTO formOrderStatusInfoDTO(Optional<Order> order){
        if (order.isPresent()) {
            OrderStatusInfoDTO orderStatusInfoDTO = new OrderStatusInfoDTO();
            orderStatusInfoDTO.setStatus(order.get().getStatus());
            orderStatusInfoDTO.setId(order.get().getId());
            return orderStatusInfoDTO;
        } else return null;
    }

    private OrderClosingResponseDTO formOrderClosingResponseDTO(long executionTime){
        OrderClosingResponseDTO closingResponseDTO = new OrderClosingResponseDTO();
        closingResponseDTO.setExecutionTime(executionTime);
        closingResponseDTO.setOrderDeliveredOnTime(executionTime < deliveryTimeStandard);
        return closingResponseDTO;
    }

    private void closeOrder(Order order, boolean isOrderCompletedOnTime){
        String orderStatus = isOrderCompletedOnTime ? "completed on time" : "completed late";
        order.setStatus(orderStatus);
        orderRepository.save(order);
    }

}
