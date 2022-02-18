package edu.senla.controller;

import edu.senla.model.dto.ClientMainInfoDTO;

import java.util.List;

public interface ClientController {

    List<ClientMainInfoDTO> getAllClients(int pages);

    ClientMainInfoDTO getClient(long id);

    void updateClient(long id, String updatedClientJson);

    void deleteClient(long id);
}

