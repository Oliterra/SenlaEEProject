package edu.senla.dao.daointerface;

import edu.senla.entity.Order;

import java.util.List;

public interface OrderRepositoryInterface extends GenericDAO<Order, Integer>{

    public List<Order> getAllClientsOrders(int clientId);

    public List<Order> getAllCouriersOrders(int courierId);

    public Order getByIdWithOrders(int orderId);

}
