package edu.senla.controller;

import edu.senla.model.dto.UserOrderInfoDTO;
import edu.senla.model.dto.CourierOrderInfoDTO;

import java.util.List;

public interface OrdersHistoryController {

    List<UserOrderInfoDTO> getClientOrdersHistory(long id);

    List<CourierOrderInfoDTO> getCourierOrdersHistory(long id);
}
