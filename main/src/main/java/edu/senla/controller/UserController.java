package edu.senla.controller;

import edu.senla.model.dto.UserMainInfoDTO;

import java.util.List;

public interface UserController {

    List<UserMainInfoDTO> getAllClients(int pages);

    UserMainInfoDTO getClient(long id);

    void updateClient(long id, String updatedClientJson);

    void deleteClient(long id);
}

