package edu.senla.dao.daointerface;

import edu.senla.entity.Client;

public interface ClientRepositoryInterface extends GenericDAO<Client, Integer>{

    public Client getByIdWithOrders(int clientId);

    public Client getClientByEmail(String clientEmail);

    public Client getClientByUsername(String clientUsername);

}

