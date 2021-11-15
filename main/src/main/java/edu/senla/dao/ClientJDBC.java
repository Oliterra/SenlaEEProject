package edu.senla.dao;

import edu.senla.entity.Client;

public interface ClientJDBC {

    public void create(Client entity);

    public Client read(int id);

    public void update(int id, Client updatedEntity);

    public void delete(int id);

}
