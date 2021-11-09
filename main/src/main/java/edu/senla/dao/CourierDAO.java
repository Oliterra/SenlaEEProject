package edu.senla.dao;

import edu.senla.entity.Courier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CourierDAO implements DAO<Courier>{

    private List<Courier> couriers = new ArrayList<>();

    @Override
    public void create(Courier entity) {
        couriers.add(entity);
    }

    @Override
    public Courier read(int id) {
        return couriers.get(id);
    }

    @Override
    public void update(Courier updatedEntity) {

    }

    @Override
    public void delete(int id) {
        couriers.remove(id);
    }

}
