package edu.senla.dao.daointerface;

import edu.senla.dto.ClientDTO;
import edu.senla.entity.Courier;

public interface CourierRepositoryInterface extends GenericDAO<Courier, Integer>{

    public int getIdByPhone(String courierPhone);

    public Courier getByIdWithOrders(int courierId);

    public Courier getByIdWithOrdersJPQL(int courierId);

}
