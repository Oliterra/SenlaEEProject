package edu.senla.dao;

import edu.senla.dao.daointerface.TypeOfContainerRepositoryInterface;
import edu.senla.entity.TypeOfContainer;
import org.springframework.stereotype.Repository;

@Repository
public class TypeOfContainerRepository extends AbstractDAO<TypeOfContainer, Integer> implements TypeOfContainerRepositoryInterface {

    public TypeOfContainerRepository() {
        super(TypeOfContainer.class);
    }

}

