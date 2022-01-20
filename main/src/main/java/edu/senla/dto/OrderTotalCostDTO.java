package edu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderTotalCostDTO {

    private double orderTotalCost;

    private List<ContainerComponentsNamesDTO> containerComponentsDTOS;

}
