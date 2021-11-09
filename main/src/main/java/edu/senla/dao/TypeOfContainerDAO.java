package edu.senla.dao;

import edu.senla.entity.TypeOfContainer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TypeOfContainerDAO implements DAO<TypeOfContainer>{

    private List<TypeOfContainer> typesOfContainer = new ArrayList<>();

    @Override
    public void create(TypeOfContainer entity) {
        typesOfContainer.add(entity);
    }

    @Override
    public TypeOfContainer read(int id) {
        return typesOfContainer.get(id);
    }

    @Override
    public void update(TypeOfContainer updatedEntity) {

    }

    @Override
    public void delete(int id) {
        typesOfContainer.remove(id);
    }

}
