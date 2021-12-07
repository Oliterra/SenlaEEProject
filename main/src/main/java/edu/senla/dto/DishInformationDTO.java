package edu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DishInformationDTO {

    public DishInformationDTO(int id, String description, int caloricContent) {
        this.id = id;
        this.description = description;
        this.caloricContent = caloricContent;
    }

    private int id;

    private int dishId;

    private String description;

    private int caloricContent;

    private LocalDate cookingDate;

    private LocalDate expirationType;

}
