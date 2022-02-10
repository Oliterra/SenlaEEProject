package edu.senla.controller;

import edu.senla.model.dto.OrderDTO;

import java.util.List;

public interface OrderController {

    List<OrderDTO> getAllOrders(int pages);

    OrderDTO getOrder(long id);

    void deleteOrder(long id);
}
