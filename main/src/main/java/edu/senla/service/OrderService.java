package edu.senla.service;

import edu.senla.model.dto.OrderClosingResponseDTO;
import edu.senla.model.dto.OrderDTO;
import edu.senla.model.dto.OrderTotalCostDTO;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getAllOrders(int pages);

    OrderTotalCostDTO checkIncomingOrderDataAndCreateIfItIsCorrect(long clientId, String shoppingCartJson);

    OrderDTO getOrder(long id);

    void deleteOrder(long id);

    OrderClosingResponseDTO closeOrderForCourier(long id);

    void closeOrderForClient(long clientId, long orderId);
}
