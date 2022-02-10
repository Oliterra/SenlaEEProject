package edu.senla.dao;

import edu.senla.model.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    Dish getByName(String name);

    @Query("SELECT dish.name FROM Dish dish WHERE dish.id =?1")
    String getNameById(long id);
}
