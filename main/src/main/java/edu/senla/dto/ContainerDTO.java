package edu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContainerDTO {

    private ContainerComponentsDTO dishes;

    private double weightOfMeat;

    private double weightOfGarnish;

    private double weightOfSalad;

    private double weightOfSauce;

}
