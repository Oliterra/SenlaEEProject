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

    boolean isCourierExists(String phone);

    boolean isCourierExists(long id);

    boolean isCourierActiveNow(long id);

    String changeCourierStatus(long id);

    List<CourierOrderInfoDTO> getAllOrdersOfCourier(long courierId);

    int assignOrdersToAllActiveCouriers(List<CourierBasicInfoDTO> courierBasicInfoDTOS);

    long assignNewOrdersToCourier(long id);

    CourierPerformanceIndicatorDTO calculateCourierPerformanceIndicator(long id);

}
