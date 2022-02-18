package edu.senla.controller;

import edu.senla.model.dto.ClientOrderInfoDTO;
import edu.senla.model.dto.CourierOrderInfoDTO;

import java.util.List;

public interface OrdersHistoryController {

    List<ClientOrderInfoDTO> getClientOrdersHistory(long id);

    List<CourierOrderInfoDTO> getCourierOrdersHistory(long id);
}
