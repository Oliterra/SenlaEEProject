package edu.senla.service;

import edu.senla.model.dto.*;

import java.util.List;

public interface CourierService {

    List<CourierMainInfoDTO> getAllCouriers(int pages);

    List<CourierBasicInfoDTO> getAllActiveCouriersDTO();

    void createCourier(String courierRegistrationRequestJson);

    CourierMainInfoDTO getCourier(long id);

    CourierBasicInfoDTO getCourierBasicInfo(long id);

    CourierCurrentOrderInfoDTO getCurrentOrderForCourier(long id);

    CourierFullInfoDTO getCourierByPhoneAndPassword(String authRequestCourierJson);

    void updateCourier(long id, String updatedCourierJson);

    void deleteCourier(long id);

    long getCurrentCourierId();

    void changeCourierStatus(long id);

    List<CourierOrderInfoDTO> getAllOrdersOfCourier(long courierId);

    void assignOrdersToAllActiveCouriers(List<CourierBasicInfoDTO> courierBasicInfoDTOS);

    void assignNewOrdersToCourier(long id);

    CourierPerformanceIndicatorDTO getCourierPerformanceIndicator(long id);
}
