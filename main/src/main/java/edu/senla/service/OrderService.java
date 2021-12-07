package edu.senla.service;

import edu.senla.dao.daointerface.ClientRepositoryInterface;
import edu.senla.dao.daointerface.CourierRepositoryInterface;
import edu.senla.dao.daointerface.OrderRepositoryInterface;
import edu.senla.dto.OrderDTO;
import edu.senla.entity.Courier;
import edu.senla.entity.Order;
import edu.senla.service.serviceinterface.OrderServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService implements OrderServiceInterface {

    private final OrderRepositoryInterface orderRepository;

    private final ClientRepositoryInterface clientRepository;

    private final CourierRepositoryInterface courierRepository;

    private final ModelMapper mapper;

    @Override
    public OrderDTO createOrder(int clientId, OrderDTO newOrderDTO) {
        Order newOrder = mapper.map(newOrderDTO, Order.class);
        newOrder.setClient(clientRepository.read(clientId));
        Order createdOrder = orderRepository.create(newOrder);
        return mapper.map(createdOrder, OrderDTO.class);
    }

    @Override
    public OrderDTO readOrder(int id) {
        Order requestedOrder = orderRepository.read(id);
        return mapper.map(requestedOrder, OrderDTO.class);
    }

    @Override
    public OrderDTO updateOrder(int id, OrderDTO orderDTO) {
        Order updatedOrder = mapper.map(orderDTO, Order.class);
        Order orderToUpdate = mapper.map(readOrder(id), Order.class);

        Order orderWithNewParameters = updateOrdersOptions(orderToUpdate, updatedOrder);
        Order order = orderRepository.update(orderWithNewParameters);

        return mapper.map(order, OrderDTO.class);
    }

    @Override
    public void deleteOrder(int id) {
        orderRepository.delete(id);
    }

    @Override
    public void setCourierOnOrder(int orderId, int courierId) {
        Courier courierForOrder = courierRepository.read(courierId);
        Order orderToSetCourier = orderRepository.read(orderId);
        orderToSetCourier.setCourier(courierForOrder);
        orderRepository.update(orderToSetCourier);
    }

    @Override
    public List<OrderDTO> getAllClientsOrders(int clientId) {
        List<Order> orders = orderRepository.getAllClientsOrders(clientId);
        List<OrderDTO> ordersDTO = new ArrayList<>();
        for (Order order: orders) {
            ordersDTO.add(mapper.map(order, OrderDTO.class));
        }
        return ordersDTO;
    }

    @Override
    public List<OrderDTO> getAllCouriersOrders(int courierId) {
        List<Order> orders = orderRepository.getAllCouriersOrders(courierId);
        List<OrderDTO> ordersDTO = new ArrayList<>();
        for (Order order: orders) {
            ordersDTO.add(mapper.map(order, OrderDTO.class));
        }
        return ordersDTO;
    }

    private Order updateOrdersOptions(Order order, Order updatedOrder) {
        order.setDate(updatedOrder.getDate());
        order.setPaymentType(updatedOrder.getPaymentType());
        order.setStatus(updatedOrder.getStatus());
        return order;
    }

}
