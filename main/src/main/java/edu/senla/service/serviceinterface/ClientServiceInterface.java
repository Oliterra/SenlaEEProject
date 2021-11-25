package edu.senla.service.serviceinterface;

import edu.senla.dto.ClientDTO;
import edu.senla.entity.Client;

public interface ClientServiceInterface {

    public void createClient(ClientDTO newClientDTO);

    public ClientDTO readClient(int id);

    public void updateClient(int id, ClientDTO updatedClientDTO);

    public void deleteClient(int id);

    public int getClientIdByEmail(String clientEmail);

    public ClientDTO getByIdWithOrders(int clientId);

    public ClientDTO getByIdWithOrdersJPQL(int clientId);

}
