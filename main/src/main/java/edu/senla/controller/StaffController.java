package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.StaffControllerInterface;
import edu.senla.dto.CourierBasicInfoDTO;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/staff")
public class StaffController implements StaffControllerInterface {

    private final CourierServiceInterface courierService;

    private final ObjectMapper mapper;

    @SneakyThrows
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getCouriersPerformanceIndicator(@PathVariable long id) {
        String courierPerformanceIndicatorJson = mapper.writeValueAsString(courierService.getCourierPerformanceIndicator(id));
        return new ResponseEntity<String>(courierPerformanceIndicatorJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> assignOrdersToAllCouriers() {
        List<CourierBasicInfoDTO> courierBasicInfoDTOS = courierService.getAllActiveCouriersDTO();
        courierService.assignOrdersToAllActiveCouriers(courierBasicInfoDTOS);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
