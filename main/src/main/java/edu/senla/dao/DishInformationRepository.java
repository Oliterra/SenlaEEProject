package edu.senla.dao;

import edu.senla.dao.daointerface.DishInformationRepositoryInterface;
import edu.senla.entity.DishInformation;
import org.springframework.stereotype.Repository;

@Repository
public class DishInformationRepository extends AbstractDAO<DishInformation, Integer> implements DishInformationRepositoryInterface {

    public DishInformationRepository() {
        super(DishInformation.class);
    }

}
