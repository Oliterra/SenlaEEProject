package edu.senla.dao.daointerface;

import edu.senla.entity.TypeOfContainer;

public interface TypeOfContainerRepositoryInterface extends GenericDAO<TypeOfContainer, Integer>{

    public TypeOfContainer getTypeOfContainerByCaloricContent(int typeOfContainerCaloricContent);

}
