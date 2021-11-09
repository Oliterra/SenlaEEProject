package edu.senla.dao;

import edu.senla.entity.DishInformation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DishInformationDAO implements DAO<DishInformation>{

    private List<DishInformation> dishesInformation = new ArrayList<>();

    @Override
    public void create(DishInformation entity) {
        dishesInformation.add(entity);
    }

    @Override
    public DishInformation read(int id) {
        return dishesInformation.get(id);
    }

    @Override
    public void update(DishInformation updatedEntity) {

    }

    @Override
    public void delete(int id) {
        dishesInformation.remove(id);
    }

}
