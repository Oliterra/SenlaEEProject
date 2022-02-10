package edu.senla.controller;

import edu.senla.model.dto.OrderTotalCostDTO;

public interface ShoppingCartController {

    OrderTotalCostDTO makeOrder(String shoppingCartJson);
}
