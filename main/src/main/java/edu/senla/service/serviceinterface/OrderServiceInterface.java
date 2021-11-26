package edu.senla.service.serviceinterface;

import edu.senla.dto.OrderDTO;
import edu.senla.entity.Order;

import java.util.List;

public interface OrderServiceInterface {

    public int createOrder(int clientId, OrderDTO newOrderDTO);

    public OrderDTO readOrder(int id);

    public void updateOrder(int id, OrderDTO updatedOrderDTO);

    public void deleteOrder(int id);

    public void setOrderCourier(OrderDTO order, int courierId);

    public List<OrderDTO> getAllClientsOrders(int clientId);

    public List<OrderDTO> getAllCouriersOrders(int courierId);

    public OrderDTO getByIdWithWithTypeOfContainer(int orderId);

}
