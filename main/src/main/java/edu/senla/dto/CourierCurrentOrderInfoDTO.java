package edu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourierCurrentOrderInfoDTO {

    private String clientFirstName;

    private String clientLastName;

    private String address;

    private LocalTime time;

    private String paymentType;

    private double orderCost;

    private List<ContainerComponentsNamesDTO> containers;

}
