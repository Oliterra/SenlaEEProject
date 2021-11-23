package edu.senla.dao.daointerface;

import edu.senla.entity.Client;

public interface ClientRepositoryInterface extends GenericDAO<Client, Integer>{

    public int getIdByEmail(String clientEmail);

}
