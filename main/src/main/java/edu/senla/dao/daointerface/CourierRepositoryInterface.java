package edu.senla.dao.daointerface;

import edu.senla.entity.Courier;

public interface CourierRepositoryInterface extends GenericDAO<Courier, Integer>{

    public Courier getCourierByPhone(String courierPhone);

    public Courier getByIdWithOrders(int courierId);

}
