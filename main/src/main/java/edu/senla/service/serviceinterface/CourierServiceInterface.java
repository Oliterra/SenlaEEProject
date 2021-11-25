package edu.senla.service.serviceinterface;

import edu.senla.dto.ClientDTO;
import edu.senla.dto.CourierDTO;
import edu.senla.entity.Courier;

public interface CourierServiceInterface {

    public void createCourier(CourierDTO newCourierDTO);

    public CourierDTO readCourier(int id);

    public void updateCourier(int id, CourierDTO updatedCourierDTO);

    public void deleteCourier(int id);

    public int getCourierIdByPhone(String courierPhone);

    public String getByIdWithOrders(int courierId);

    public CourierDTO getByIdWithOrdersJPQL(int courierId);

}
