package edu.senla.service.serviceinterface;

import edu.senla.dto.CourierDTO;

public interface CourierServiceInterface {

    public CourierDTO createCourier(CourierDTO newCourierDTO);

    public CourierDTO readCourier(long id);

    public CourierDTO updateCourier(long id, CourierDTO updatedCourierDTO);

    public void deleteCourier(long id);

    public CourierDTO getByIdWithOrders(long courierId);

    public boolean isCourierExists(CourierDTO courier);

}
