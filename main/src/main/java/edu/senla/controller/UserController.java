package edu.senla.controller;

import edu.senla.model.dto.UserMainInfoDTO;

import java.util.List;

public interface UserController {

    List<UserMainInfoDTO> getAllUsers(int pages);

    UserMainInfoDTO getUser(long id);

    void updateUser(long id, String updatedClientJson);

    void deleteUser(long id);
}

