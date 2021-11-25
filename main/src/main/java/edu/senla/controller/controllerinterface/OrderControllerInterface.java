package edu.senla.controller.controllerinterface;

import edu.senla.dto.OrderDTO;

import java.util.List;

public interface OrderControllerInterface {

    public int createOrder(int clientId, String newOrderJson);

    public String readOrder(int id);

    public void updateOrder(int id, String updatedOrderJson);

    public void deleteOrder(int id);

    public void setOrderCourier(int orderId, int courierId);

    public String getAllClientsOrders(int clientId);

    public String getAllCouriersOrders(int courierId);

    public String getByIdWithWithTypeOfContainer(int orderId);

}
