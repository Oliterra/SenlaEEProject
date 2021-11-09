package edu.senla.dao;

import edu.senla.entity.Dish;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DishDAO implements DAO<Dish>{

    private List<Dish> dishes = new ArrayList<>();

    @Override
    public void create(Dish entity) {
        dishes.add(entity);
    }

    @Override
    public Dish read(int id) {
        return dishes.get(id);
    }

    @Override
    public void update(Dish updatedEntity) {

    }

    @Override
    public void delete(int id) {
        dishes.remove(id);
    }

}
