package edu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {

    public OrderDTO(int id, String paymentType, String status) {
        this.id = id;
        this.paymentType = paymentType;
        this.status = status;
    }

    private int id;

    private int clientId;

    private String paymentType;

    private String status;

    private LocalDate date;

}
