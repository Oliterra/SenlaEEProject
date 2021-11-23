package edu.senla.dao;

import edu.senla.dao.daointerface.DishInformationRepositoryInterface;
import edu.senla.dao.daointerface.DishRepositoryInterface;
import edu.senla.entity.Client;
import edu.senla.entity.Courier;
import edu.senla.entity.DishInformation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DishInformationRepository extends AbstractDAO<DishInformation, Integer> implements DishInformationRepositoryInterface {

    public DishInformationRepository() {
        super(DishInformation.class);
    }

}
