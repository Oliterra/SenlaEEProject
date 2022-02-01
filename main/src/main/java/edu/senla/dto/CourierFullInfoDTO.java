package edu.senla.dto;

import edu.senla.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourierFullInfoDTO {

    private String firstName;

    private String lastName;

    private String phone;

    private String password;

    private Role role;

}
