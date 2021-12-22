package edu.senla.dao;

import edu.senla.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepositoryInterface extends JpaRepository<Dish, Long> {

    public Dish getDishByName(String name);

}
