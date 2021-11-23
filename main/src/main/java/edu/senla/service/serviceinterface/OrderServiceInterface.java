package edu.senla.service.serviceinterface;

import edu.senla.dto.OrderDTO;
import edu.senla.entity.Order;

public interface OrderServiceInterface {

    public void createOrder(OrderDTO newOrderDTO);

    public OrderDTO readOrder(int id);

    public void updateOrder(int id, OrderDTO updatedOrderDTO);

    public void deleteOrder(int id);

}
