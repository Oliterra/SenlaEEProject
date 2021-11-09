package edu.senla.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DishInformation {

    private int id;

    private String description;

    private int proteins;

    private int fats;

    private int carbohydrates;

    private int caloricContent;

    private Date cookingDate;

    private Date expirationDate;

}
