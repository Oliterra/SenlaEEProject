package edu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {

    private int clientId;

    private int courierId;

    private LocalDate date;

    private LocalTime time;

    private String paymentType;

    private String status;

}
