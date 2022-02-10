package edu.senla.controller;

import edu.senla.model.dto.CourierPerformanceIndicatorDTO;

public interface StaffController {

    CourierPerformanceIndicatorDTO getCouriersPerformanceIndicator(long id);

    void assignOrdersToAllCouriers();
}
