package edu.senla.controller.controllerinterface;

public interface CourierControllerInterface {

    public void createCourier(String newCourierJson);

    public String readCourier(int id);

    public void updateCourier(int id, String updatedCourierJson);

    public void deleteCourier(int id);

    public int getCourierIdByPhone(String courierPhone);

}
