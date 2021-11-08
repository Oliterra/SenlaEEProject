package edu.senla.controller;

import edu.senla.dto.ClientDTO;

public interface ClientControllerInterface {

    public void createClient(String clientJson);

    public String read(ClientDTO clientToReadDTO);

    public void update(ClientDTO clientToUpdateDTO, String updatedClientJson);

    public void delete(ClientDTO clientToDeleteDTO);

}
