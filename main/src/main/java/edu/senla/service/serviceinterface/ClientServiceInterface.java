package edu.senla.service.serviceinterface;

import edu.senla.dto.ClientDTO;
import edu.senla.entity.Client;

public interface ClientServiceInterface {

    public void createClient(ClientDTO newClientDTO);

    public ClientDTO read(int id);

    public void update(int id, ClientDTO updatedClientDTO);

    public void delete(int id);

}
