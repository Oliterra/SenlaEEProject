package edu.senla.service.serviceinterface;

import edu.senla.dto.ClientDTO;

public interface ClientServiceInterface {

    public ClientDTO createClient(ClientDTO newClientDTO);

    public ClientDTO readClient(long id);

    public ClientDTO updateClient(long id, ClientDTO updatedClientDTO);

    public void deleteClient(long id);

    public ClientDTO getByIdWithOrders(long clientId);

    public boolean isClientExists(ClientDTO client);

    public ClientDTO getClientByUsernameAndPassword(String username, String password);

}

