package edu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderFullInfoDTO {

    private ShoppingCartDTO shoppingCart;

    private String paymentType;

}
