package edu.senla.service;

import edu.senla.dao.daointerface.OrderRepositoryInterface;
import edu.senla.dto.OrderDTO;
import edu.senla.entity.Order;
import edu.senla.service.serviceinterface.OrderServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class OrderService implements OrderServiceInterface {

    private OrderRepositoryInterface orderRepository;

    //@Autowired
    //private ClientRepository clientRepository;

    /*@Autowired
    private CourierRepository courierRepository;*/

    private ModelMapper mapper;

    /*@Override
    public int createOrder(int clientId, OrderDTO newOrderDTO) {
        Order newOrder = mapper.map(newOrderDTO, Order.class);
        newOrder.setClient(clientRepository.read(clientId));
        Order order = orderRepository.create(newOrder);
        return order.getId();
    }*/

    @Override
    public int createOrder(int clientId, OrderDTO newOrderDTO) {
        return 0;
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

    @Override
    public void setOrderCourier(OrderDTO order, int courierId) {

    }

    /*@Override
    public void setOrderCourier(OrderDTO order, int courierId) {
        Order orderEntity = mapper.map(order, Order.class);
        orderEntity.setCourier(courierRepository.read(courierId));
    }*/

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

    @Override
    public OrderDTO getByIdWithWithTypeOfContainer(int orderId) {
        return null;
    }

    /*@Override
    public OrderDTO getByIdWithWithTypeOfContainer(int orderId) {
        return mapper.map(orderRepository.getByIdWithWithTypeOfContainer(orderId), OrderDTO.class);
    }*/

    private Order updateOrdersOptions(Order order, Order updatedOrder) {
        order.setDate(updatedOrder.getDate());
        order.setPaymentType(updatedOrder.getPaymentType());
        order.setStatus(updatedOrder.getStatus());
        return order;
    }

}
