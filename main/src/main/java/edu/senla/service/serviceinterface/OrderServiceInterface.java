package edu.senla.service.serviceinterface;

import edu.senla.dto.OrderClosingResponseDTO;
import edu.senla.dto.OrderDTO;
import edu.senla.dto.OrderForUpdateDTO;
import edu.senla.dto.OrderStatusInfoDTO;

import java.util.List;

public interface OrderServiceInterface {

    List<OrderDTO> getAllOrders();

    OrderDTO getOrder(long id);

    void updateOrder(long id, OrderForUpdateDTO updatedOrderDTO);

    void deleteOrder(long id);

    OrderStatusInfoDTO getOrderStatusInfo(long courierId);

    boolean isOrderIsInProcess(long id);

    boolean isOrderConfirmedByClient(long id);

    OrderClosingResponseDTO closeOrderForCourier(long courierId);

    void closeOrderForClient(long id);

    boolean isOrderBelongToClient(long id, long clientId);

    boolean isOrderExists(long id);

}
