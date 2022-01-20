package edu.senla.service.serviceinterface;

import edu.senla.dto.*;

import java.util.List;

public interface CourierServiceInterface {

    List<CourierMainInfoDTO> getAllCouriers();

    List<CourierBasicInfoDTO> getAllActiveCouriersDTO();

    void createCourier(CourierRegistrationRequestDTO newCourierDTO);

    CourierMainInfoDTO getCourier(long id);

    CourierBasicInfoDTO getCourierBasicInfo(long id);

    CourierCurrentOrderInfoDTO getCurrentOrderForCourier(long id);

    CourierFullInfoDTO getCourierByPhoneAndPassword(String phone, String password);

    void updateCourier(long id, CourierMainInfoDTO updatedCourierDTO);

    void deleteCourier(long id);

    long getCurrentCourierId();

    void changeCourierStatus(long id);

    List<CourierOrderInfoDTO> getAllOrdersOfCourier(long courierId);

    void assignOrdersToAllActiveCouriers(List<CourierBasicInfoDTO> courierBasicInfoDTOS);

    void assignNewOrdersToCourier(long id);

    CourierPerformanceIndicatorDTO getCourierPerformanceIndicator(long id);

}
