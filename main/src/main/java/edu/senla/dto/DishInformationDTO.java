package edu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DishInformationDTO {

    private int id;

    private String description;

    private int caloricContent;

    private LocalDate cookingDate;

    private LocalDate expirationType;

}
