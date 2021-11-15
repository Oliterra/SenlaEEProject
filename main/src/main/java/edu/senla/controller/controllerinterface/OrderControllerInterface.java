package edu.senla.controller.controllerinterface;

public interface OrderControllerInterface {

    public void createOrder(String newOrderJson);

    public String readOrder(int id);

    public void updateOrder(int id, String updatedOrderJson);

    public void deleteOrder(int id);

}
