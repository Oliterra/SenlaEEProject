package edu.senla.dao;

import edu.senla.model.entity.Dish;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository {

    List<Dish> findAll();

    Dish save(Dish dishEntity);

    Dish getById(long id);

    void update(long id, Dish updatedDishEntity);

    void deleteById(long id);

    boolean existsById(long id);

    Dish getByName(String name);

    String getNameById(long id);
}
