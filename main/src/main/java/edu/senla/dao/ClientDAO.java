package edu.senla.dao;

import edu.senla.entity.Client;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientDAO implements DAO<Client>{

    public List<Client> clients = new ArrayList<>();

    @Override
    public void create(Client entity) {
        clients.add(entity);
    }

    @Override
    public Client read(int id) {
        return clients.get(id);
    }

    @Override
    public void update(Client updatedEntity) {
        clients.add(updatedEntity);
    }

    @Override
    public void delete(int id) {
        clients.remove(id);
    }

}
