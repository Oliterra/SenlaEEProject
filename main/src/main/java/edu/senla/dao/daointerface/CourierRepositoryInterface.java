package edu.senla.dao.daointerface;

import edu.senla.entity.Courier;

public interface CourierRepositoryInterface extends GenericDAO<Courier, Integer>{

    public int getIdByPhone(String courierPhone);

}
