package edu.senla.dao;

import edu.senla.model.entity.Dish;
import org.springframework.data.jpa.repository.Query;
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

    @Query("SELECT dish.name FROM Dish dish WHERE dish.id =?1")
    String getNameById(long id);
}
