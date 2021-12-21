package edu.senla.service.serviceinterface;

import edu.senla.dto.ClientDTO;

public interface ClientServiceInterface {

    public ClientDTO createClient(ClientDTO newClientDTO);

    public ClientDTO readClient(int id);

    public ClientDTO updateClient(int id, ClientDTO updatedClientDTO);

    public void deleteClient(int id);

    public ClientDTO getByIdWithOrders(int clientId);

    public boolean isClientExists(ClientDTO client);

    public ClientDTO getClientByUsernameAndPassword(String username, String password);

}

