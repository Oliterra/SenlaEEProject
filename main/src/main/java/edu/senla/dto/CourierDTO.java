package edu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourierDTO {

    private int id;

    private String firstName;

    private String lastName;

    private String phone;

}
