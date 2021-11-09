package edu.senla.service.serviceinterface;

import edu.senla.dto.CourierDTO;
import edu.senla.entity.Courier;

public interface CourierServiceInterface {

    public void createCourier(CourierDTO newCourierDTO);

    public CourierDTO read(int id);

    public Courier update(CourierDTO courierToUpdateDTO, CourierDTO updatedCourierDTO);

    public void delete(int id);

}
