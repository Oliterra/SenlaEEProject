package edu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TypeOfContainerDTO {

    private int numberOfCalories;

    private String name;

    private int price;

}
