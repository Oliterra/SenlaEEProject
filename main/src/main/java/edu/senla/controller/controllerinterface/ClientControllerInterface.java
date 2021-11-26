package edu.senla.controller.controllerinterface;

import edu.senla.dto.ClientDTO;

public interface ClientControllerInterface {

    public void createClient(String newClientJson);

    public String readClient(int id);

    public void updateClient(int id, String updatedClientJson);

    public void deleteClient(int id);

    public int getClientIdByEmail(String clientEmail);

    public String getByIdWithOrders(int clientId);

    public String getByIdWithOrdersJPQL(int clientId);

}
