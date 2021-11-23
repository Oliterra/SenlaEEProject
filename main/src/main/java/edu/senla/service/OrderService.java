package edu.senla.service;

import edu.senla.dao.ClientRepository;
import edu.senla.dao.CourierRepository;
import edu.senla.dao.daointerface.OrderRepositoryInterface;
import edu.senla.dto.OrderDTO;
import edu.senla.entity.Order;
import edu.senla.service.serviceinterface.OrderServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService implements OrderServiceInterface {

    private final OrderRepositoryInterface orderRepository;

    private final ClientRepository clientRepository;

    private final CourierRepository courierRepository;

    private final ModelMapper mapper;

    @Override
    public void createOrder(OrderDTO newOrderDTO) {
        int clientId = newOrderDTO.getClient_id();
        int courierId = newOrderDTO.getCourier_id();
        Order newOrder = mapper.map(newOrderDTO, Order.class);
        newOrder.setClient(clientRepository.read(clientId));
        newOrder.setCourier(courierRepository.read(courierId));
        orderRepository.create(newOrder);
    }

    @Override
    public OrderDTO readOrder(int id) {
        Order requestedOrder = orderRepository.read(id);
        return mapper.map(requestedOrder, OrderDTO.class);
    }

    @Override
    public void updateOrder(int id, OrderDTO updatedOrderDTO) {
        Order updatedOrder = mapper.map(updatedOrderDTO, Order.class);
        Order orderToUpdate = mapper.map(readOrder(id), Order.class);
        Order orderWithNewParameters = updateOrdersOptions(orderToUpdate, updatedOrder);
        orderRepository.update(orderWithNewParameters);
    }

    @Override
    public void deleteOrder(int id) {
        orderRepository.delete(id);
    }

    private Order updateOrdersOptions(Order order, Order updatedOrder)
    {
        order.setClient(updatedOrder.getClient());
        order.setCourier(updatedOrder.getCourier());
        order.setDate(updatedOrder.getDate());
        order.setTime(updatedOrder.getTime());
        order.setPaymentType(updatedOrder.getPaymentType());
        order.setStatus(updatedOrder.getStatus());
        return order;
    }

}
