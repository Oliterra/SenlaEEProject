package edu.senla.controller;

import edu.senla.model.dto.OrderClosingResponseDTO;

public interface OrderConfirmationController {

    void confirmReceiptOfTheOrderClient(long orderId);

    OrderClosingResponseDTO closeOrderCourier();
}
