package edu.senla.service.serviceinterface;

import edu.senla.dto.OrderDTO;

import java.util.List;

public interface OrderServiceInterface {

    public OrderDTO createOrder(long clientId, OrderDTO newOrderDTO);

    public OrderDTO readOrder(long id);

    public OrderDTO updateOrder(long id, OrderDTO updatedOrderDTO);

    public void deleteOrder(long id);

    public void setCourierOnOrder(long orderId, long courierId);

    public List<OrderDTO> getAllClientsOrders(long clientId);

    public List<OrderDTO> getAllCouriersOrders(long courierId);

}
