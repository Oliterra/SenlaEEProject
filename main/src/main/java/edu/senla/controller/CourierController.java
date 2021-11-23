package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.CourierControllerInterface;
import edu.senla.dto.ClientDTO;
import edu.senla.dto.CourierDTO;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourierController implements CourierControllerInterface {

    private final CourierServiceInterface courierService;

    private final ObjectMapper jacksonObjectMapper;

    @SneakyThrows
    @Override
    public void createCourier(String newCourierJson) {
        CourierDTO newCourierDTO = jacksonObjectMapper.readValue(newCourierJson, CourierDTO.class);
        courierService.createCourier(newCourierDTO);
        System.out.println("Courier" + readCourier(getCourierIdByPhone(newCourierDTO.getPhone())) + " was successfully created");
    }

    @SneakyThrows
    @Override
    public String readCourier(int id) {
        CourierDTO courierDTO = courierService.readCourier(id);
        return jacksonObjectMapper.writeValueAsString(courierDTO);
    }

    @SneakyThrows
    @Override
    public void updateCourier(int id, String updatedCourierJson) {
        CourierDTO updatedCourierDTO = jacksonObjectMapper.readValue(updatedCourierJson, CourierDTO.class);
        courierService.updateCourier(id, updatedCourierDTO);
        System.out.println("Courier was successfully updated");
    }

    @Override
    public void deleteCourier(int id) {
        courierService.deleteCourier(id);
        System.out.println("Courier was successfully deleted");
    }

    @Override
    public int getCourierIdByPhone(String courierPhone) {
        return courierService.getCourierIdByPhone(courierPhone);
    }

}
