package edu.senla.dto;

import edu.senla.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourierDTO {

    public CourierDTO(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private int id;

    private String firstName;

    private String lastName;

    private String phone;

    private List<Order> orders;

}
