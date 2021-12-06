package edu.senla.service.serviceinterface;

import edu.senla.dto.CourierDTO;

public interface CourierServiceInterface {

    public CourierDTO createCourier(CourierDTO newCourierDTO);

    public CourierDTO readCourier(int id);

    public CourierDTO updateCourier(int id, CourierDTO updatedCourierDTO);

    public void deleteCourier(int id);

    public CourierDTO getByIdWithOrders(int courierId);

    public boolean isCourierExists(CourierDTO courier);

}
