package edu.senla.dao;

import edu.senla.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDAO implements DAO<Order>{

    private List<Order> orders = new ArrayList<>();

    @Override
    public void create(Order entity) {
        orders.add(entity);
    }

    @Override
    public Order read(int id) {
        return orders.get(id);
    }

    @Override
    public void update(Order updatedEntity) {

    }

    @Override
    public void delete(int id) {
        orders.remove(id);
    }

}
