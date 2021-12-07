package edu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DishDTO {

    public DishDTO(int id, String name, String dishType) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
    }

    private int id;

    private String name;

    private String dishType;

    private DishInformationDTO dishInformation;

}
