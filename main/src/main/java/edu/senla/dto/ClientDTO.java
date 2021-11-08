package edu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientDTO {

    private int id;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String address;

}
