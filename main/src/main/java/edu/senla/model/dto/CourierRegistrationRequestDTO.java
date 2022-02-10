package edu.senla.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourierRegistrationRequestDTO {

    private String firstName;

    private String lastName;

    private String phone;

    private String password;

    private String passwordConfirm;

}
