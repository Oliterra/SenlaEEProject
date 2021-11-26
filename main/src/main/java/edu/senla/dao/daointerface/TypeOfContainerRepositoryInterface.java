package edu.senla.dao.daointerface;

import edu.senla.entity.TypeOfContainer;

import java.util.List;

public interface TypeOfContainerRepositoryInterface extends GenericDAO<TypeOfContainer, Integer>{

    public List<TypeOfContainer> getAllContainersByCaloricContent(int caloricContent);

}
