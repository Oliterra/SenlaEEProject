package edu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourierOrderInfoDTO {

    private LocalDate date;

    private LocalTime time;

    private String paymentType;

    private String clientName;

    private double orderCost;

    private List<ContainerComponentsNamesDTO> containers;

    private boolean isOrderDeliveredOnTime;

}
