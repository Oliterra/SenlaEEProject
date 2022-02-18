package edu.senla.controller;

import edu.senla.model.dto.CourierMainInfoDTO;

import java.util.List;

public interface CourierController {

    List<CourierMainInfoDTO> getAllCouriers(int pages);

    CourierMainInfoDTO getCourier(long id);

    void updateCourier(long id, String updatedCourierJson);

    void deleteCourier(long id);
}
