package edu.senla.dao.daointerface;

import edu.senla.dto.ClientDTO;
import edu.senla.entity.Client;

public interface ClientRepositoryInterface extends GenericDAO<Client, Integer>{

    public int getIdByEmail(String clientEmail);

    public Client getByIdWithOrders(int clientId);

    public Client getByIdWithOrdersJPQL(int clientId);

}
