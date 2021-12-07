package edu.senla.service.serviceinterface;

import edu.senla.dto.OrderDTO;

import java.util.List;

public interface OrderServiceInterface {

    public OrderDTO createOrder(int clientId, OrderDTO newOrderDTO);

    public OrderDTO readOrder(int id);

    public OrderDTO updateOrder(int id, OrderDTO updatedOrderDTO);

    public void deleteOrder(int id);

    public void setCourierOnOrder(int orderId, int courierId);

    public List<OrderDTO> getAllClientsOrders(int clientId);

    public List<OrderDTO> getAllCouriersOrders(int courierId);

}
