package edu.senla.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DishInformationForUpdateDTO {

    private String description;

    private double proteins;

    private double fats;

    private double carbohydrates;

    private double caloricContent;

}
