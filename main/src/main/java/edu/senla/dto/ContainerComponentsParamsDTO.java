package edu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContainerComponentsParamsDTO {

    private double totalWeight;

    private double meatWeight;

    private double garnishWeight;

    private double saladWeight;

    private double sauceWeight;

    private double totalCaloricContent;

    private double meatCaloricContent;

    private double garnishCaloricContent;

    private double saladCaloricContent;

    private double sauceCaloricContent;

}
