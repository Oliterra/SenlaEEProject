package edu.senla.dto;

import edu.senla.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientDTO {

    public ClientDTO(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private int id;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String address;

    private String username;

    private String password;

    private List<Order> orders;

}
