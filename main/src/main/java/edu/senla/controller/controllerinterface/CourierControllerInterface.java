package edu.senla.controller.controllerinterface;

import edu.senla.dto.ClientDTO;
import edu.senla.dto.CourierDTO;

public interface CourierControllerInterface {

    public void createCourier(String newCourierJson);

    public String readCourier(int id);

    public void updateCourier(int id, String updatedCourierJson);

    public void deleteCourier(int id);

    public int getCourierIdByPhone(String courierPhone);

    public String getByIdWithOrders(int courierId);

    public String getByIdWithOrdersJPQL(int courierId);

}
