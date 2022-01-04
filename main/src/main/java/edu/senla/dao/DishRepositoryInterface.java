package edu.senla.dao;

import edu.senla.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepositoryInterface extends JpaRepository<Dish, Long> {

    public Dish getByName(String name);

    @Query("SELECT dish.name FROM Dish dish WHERE dish.id =?1")
    public String getNameById(long id);

}
