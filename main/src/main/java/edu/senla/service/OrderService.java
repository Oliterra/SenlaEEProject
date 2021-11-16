package edu.senla.service;

import edu.senla.dao.DAO;
import edu.senla.dto.OrderDTO;
import edu.senla.entity.Order;
import edu.senla.service.serviceinterface.OrderServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderServiceInterface {

    private final DAO<Order> orderDAO;

    private final ModelMapper mapper;

    @Override
    public void createOrder(OrderDTO newOrderDTO) {
        orderDAO.create(mapper.map(newOrderDTO, Order.class));
    }

    @Override
    public OrderDTO read(int id) {
        Order requestedOrder = orderDAO.read(id);
        return mapper.map(requestedOrder, OrderDTO.class);
    }

    @Override
    public Order update(int id, OrderDTO updatedOrderDTO) {
        Order updatedOrder = mapper.map(updatedOrderDTO, Order.class);
        return updateOrdersOptions(orderDAO.read(id), updatedOrder);
    }

    @Override
    public void delete(int id) {
        orderDAO.delete(id);
    }

    private Order updateOrdersOptions(Order order, Order updatedOrder)
    {
        order.setId(updatedOrder.getId());
        order.setClientId(updatedOrder.getClientId());
        order.setCourierId(updatedOrder.getCourierId());
        order.setDate(updatedOrder.getDate());
        order.setTime(updatedOrder.getTime());
        order.setPaymentType(updatedOrder.getPaymentType());
        order.setStatus(updatedOrder.getStatus());
        return order;
    }

}
