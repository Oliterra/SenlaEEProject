package edu.senla.service;

import edu.senla.dao.ClientRepositoryInterface;
import edu.senla.dao.CourierRepositoryInterface;
import edu.senla.dao.OrderRepositoryInterface;
import edu.senla.dto.OrderDTO;
import edu.senla.entity.Courier;
import edu.senla.entity.Order;
import edu.senla.service.serviceinterface.OrderServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public OrderDTO createOrder(long clientId, OrderDTO newOrderDTO) {
        Order newOrder = mapper.map(newOrderDTO, Order.class);
        newOrder.setClient(clientRepository.getById(clientId));
        Order createdOrder = orderRepository.save(newOrder);
        return mapper.map(createdOrder, OrderDTO.class);
    }

    @Override
    public OrderDTO readOrder(long id) {
        return mapper.map(orderRepository.getById(id), OrderDTO.class);
    }

    @Override
    public OrderDTO updateOrder(long id, OrderDTO orderDTO) {
        Order updatedOrder = mapper.map(orderDTO, Order.class);
        Order orderToUpdate = orderRepository.getById(id);

        Order orderWithNewParameters = updateOrdersOptions(orderToUpdate, updatedOrder);
        Order order = orderRepository.save(orderWithNewParameters);

        return mapper.map(order, OrderDTO.class);
    }

    @Override
    public void deleteOrder(long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void setCourierOnOrder(long orderId, long courierId) {
        Courier courierForOrder = courierRepository.getById(courierId);
        Order orderToSetCourier = orderRepository.getById(orderId);
        orderToSetCourier.setCourier(courierForOrder);
        orderRepository.save(orderToSetCourier);
    }

    @Override
    public List<OrderDTO> getAllClientsOrders(long clientId) {
        /*List<Order> orders = orderRepository.getAllClientsOrders(clientId);
        List<OrderDTO> ordersDTO = new ArrayList<>();
        for (Order order: orders) {
            ordersDTO.add(mapper.map(order, OrderDTO.class));
        }
        return ordersDTO;*/
        return null;
    }

    @Override
    public List<OrderDTO> getAllCouriersOrders(long courierId) {
        /*List<Order> orders = orderRepository.getAllCouriersOrders(courierId);
        List<OrderDTO> ordersDTO = new ArrayList<>();
        for (Order order: orders) {
            ordersDTO.add(mapper.map(order, OrderDTO.class));
        }
        return ordersDTO;*/
        return null;
    }

    private Order updateOrdersOptions(Order order, Order updatedOrder) {
        order.setDate(updatedOrder.getDate());
        order.setPaymentType(updatedOrder.getPaymentType());
        order.setStatus(updatedOrder.getStatus());
        return order;
    }

}
