package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.StaffControllerInterface;
import edu.senla.dto.CourierBasicInfoDTO;
import edu.senla.dto.CourierPerformanceIndicatorDTO;
import edu.senla.exeptions.NoContent;
import edu.senla.exeptions.NotFound;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
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

    private final Logger LOG = (Logger) LoggerFactory.getLogger(StaffController.class);

    @SneakyThrows
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getCouriersPerformanceIndicator(@PathVariable long id) {
        CourierBasicInfoDTO courierBasicInfoDTO = courierService.getCourierBasicInfo(id);
        if (courierBasicInfoDTO == null) {
            LOG.info("Courier with id {} not found", id);
            throw new NotFound();
        }
        LOG.info("Requested performance indicator of the courier {} {}", courierBasicInfoDTO.getFirstName(), courierBasicInfoDTO.getLastName());
        CourierPerformanceIndicatorDTO courierPerformanceIndicatorDTO = courierService.calculateCourierPerformanceIndicator(id);
        if (courierPerformanceIndicatorDTO == null){
            LOG.info("No closed orders found for courier {} {}", courierBasicInfoDTO.getFirstName(), courierBasicInfoDTO.getLastName());
            throw new NoContent();
        }
        String courierPerformanceIndicatorJson = mapper.writeValueAsString(courierPerformanceIndicatorDTO);
        return new ResponseEntity<String>(courierPerformanceIndicatorJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> changeAnyStatus(@PathVariable long id) {
        CourierBasicInfoDTO courierBasicInfo = courierService.getCourierBasicInfo(id);
        if (courierBasicInfo == null) {
            LOG.info("Courier with id {} not found", id);
            throw new NotFound();
        }
        String status = courierService.changeCourierStatus(id);
        LOG.info("Courier {} {} is {}", courierBasicInfo.getFirstName(), courierBasicInfo.getLastName(), status);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> assignOrdersToAllCouriers() {
        List<CourierBasicInfoDTO> courierBasicInfoDTOS = courierService.getAllActiveCouriersDTO();
        if (courierBasicInfoDTOS == null || courierBasicInfoDTOS.isEmpty()) {
            LOG.info("Currently there are no active couriers to assign orders");
            throw new NotFound();
        }
        int numberOfAssignedOrders = courierService.assignOrdersToAllActiveCouriers(courierBasicInfoDTOS);
        LOG.info("{} orders assigned to couriers, {} pending", numberOfAssignedOrders, courierBasicInfoDTOS.size() - numberOfAssignedOrders);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
