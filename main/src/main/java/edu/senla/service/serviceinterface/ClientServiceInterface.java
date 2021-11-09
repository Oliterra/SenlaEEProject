package edu.senla.service.serviceinterface;

import edu.senla.dto.ClientDTO;
import edu.senla.entity.Client;

public interface ClientServiceInterface {

    public void createClient(ClientDTO newClientDTO);

    public ClientDTO read(int id);

    public Client update(ClientDTO clientToUpdateDTO, ClientDTO updatedClientDTO);

    public void delete(int id);

}
