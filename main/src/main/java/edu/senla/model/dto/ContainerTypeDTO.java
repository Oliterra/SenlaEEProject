package edu.senla.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContainerTypeDTO {

    private long numberOfCalories;

    private String name;

    private int price;

}
