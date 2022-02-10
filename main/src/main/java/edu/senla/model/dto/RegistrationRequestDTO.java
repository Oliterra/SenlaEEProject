package edu.senla.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistrationRequestDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String username;

    private String password;

    private String passwordConfirm;

}
