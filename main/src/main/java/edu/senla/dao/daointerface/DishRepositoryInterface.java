package edu.senla.dao.daointerface;

import edu.senla.entity.Dish;

public interface DishRepositoryInterface extends GenericDAO<Dish, Integer>{

    public int getIdByName(String dishName);

}
