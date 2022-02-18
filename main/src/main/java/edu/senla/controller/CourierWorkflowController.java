package edu.senla.controller;

import edu.senla.model.dto.CourierCurrentOrderInfoDTO;

public interface CourierWorkflowController {

    CourierCurrentOrderInfoDTO getCurrentOrderInfo();

    void changeStatus();

    CourierCurrentOrderInfoDTO getNewOrder();
}
