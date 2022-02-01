package edu.senla.service.serviceinterface;

import edu.senla.dto.*;

import java.util.List;

public interface OrderServiceInterface {

    List<OrderDTO> getAllOrders();

    OrderTotalCostDTO checkIncomingOrderDataAndCreateIfItIsCorrect(long clientId, ShoppingCartDTO shoppingCartDTO);

    OrderDTO getOrder(long id);

    void deleteOrder(long id);

    OrderClosingResponseDTO closeOrderForCourier(long id);

    void closeOrderForClient(long clientId, long orderId);

}
