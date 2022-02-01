package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.CourierWorkflowControllerInterface;
import edu.senla.dto.CourierBasicInfoDTO;
import edu.senla.dto.CourierCurrentOrderInfoDTO;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workflows")
public class CourierWorkflowController implements CourierWorkflowControllerInterface {

    private final CourierServiceInterface courierService;

    private final ObjectMapper mapper;

    @SneakyThrows
    @Secured({"ROLE_COURIER"})
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public ResponseEntity<String> getCurrentOrderInfo() {
        long id = courierService.getCurrentCourierId();
        CourierCurrentOrderInfoDTO currentOrderInfoDTO = courierService.getCurrentOrderForCourier(id);
        String currentOrderInfoJson = mapper.writeValueAsString(currentOrderInfoDTO);
        return new ResponseEntity<String>(currentOrderInfoJson, HttpStatus.OK);
    }


    @Secured({"ROLE_COURIER"})
    @RequestMapping(value = "/status", method = RequestMethod.PUT)
    public ResponseEntity<Void> changeStatus() {
        long id = courierService.getCurrentCourierId();
        CourierBasicInfoDTO courierBasicInfo = courierService.getCourierBasicInfo(id);
        courierService.changeCourierStatus(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @SneakyThrows
    @Secured({"ROLE_COURIER"})
    @RequestMapping(value = "/orders", method = RequestMethod.PUT)
    public ResponseEntity<String> getNewOrder() {
        long id = courierService.getCurrentCourierId();
        courierService.assignNewOrdersToCourier(id);
        CourierCurrentOrderInfoDTO currentOrderInfoDTO = courierService.getCurrentOrderForCourier(id);
        String currentOrderInfoJson = mapper.writeValueAsString(currentOrderInfoDTO);
        return new ResponseEntity<String>(currentOrderInfoJson, HttpStatus.OK);
    }

}
