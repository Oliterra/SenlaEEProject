package edu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DishInformationDTO {

    private long dishId;

    private String description;

    private double proteins;

    private double fats;

    private double carbohydrates;

    private double caloricContent;

}
