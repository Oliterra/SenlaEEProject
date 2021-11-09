package edu.senla.service.serviceinterface;

import edu.senla.dto.OrderDTO;
import edu.senla.entity.Order;

public interface OrderServiceInterface {

    public void createOrder(OrderDTO newOrderDTO);

    public OrderDTO read(int id);

    public Order update(OrderDTO orderToUpdateDTO, OrderDTO updatedOrderDTO);

    public void delete(int id);

}
