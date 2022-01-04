package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.CourierWorkflowControllerInterface;
import edu.senla.dto.*;
import edu.senla.exeptions.BadRequest;
import edu.senla.exeptions.NoContent;
import edu.senla.exeptions.NotFound;
import edu.senla.service.OrderService;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workflows")
public class CourierWorkflowController implements CourierWorkflowControllerInterface {

    private final CourierServiceInterface courierService;

    private final OrderService orderService;

    private final ObjectMapper mapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(CourierWorkflowController.class);

    @SneakyThrows
    @Secured({"ROLE_COURIER"})
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public ResponseEntity<String> getCurrentOrderInfo() {
        long id = courierService.getCurrentCourierId();
        CourierBasicInfoDTO courierBasicInfo = courierService.getCourierBasicInfo(id);
        if (!courierService.isCourierActiveNow(id)) {
            LOG.info("Error. Courier {} {} is inactive now", courierBasicInfo.getFirstName(), courierBasicInfo.getLastName());
            throw new BadRequest();
        }
        CourierCurrentOrderInfoDTO currentOrderInfoDTO = courierService.getCurrentOrderForCourier(id);
        if (currentOrderInfoDTO == null) {
            LOG.info("Courier {} {} has no current order", courierBasicInfo.getFirstName(), courierBasicInfo.getLastName());
            throw new NoContent();
        }
        LOG.info("Current order of courier {} {}: {}", courierBasicInfo.getFirstName(), courierBasicInfo.getLastName(), currentOrderInfoDTO);
        String currentOrderInfoJson = mapper.writeValueAsString(currentOrderInfoDTO);
        return new ResponseEntity<String>(currentOrderInfoJson, HttpStatus.OK);
    }

    @SneakyThrows
    @Secured({"ROLE_COURIER"})
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity<String> getOrdersHistory() {
        long id = courierService.getCurrentCourierId();
        CourierBasicInfoDTO courierBasicInfoDTO = courierService.getCourierBasicInfo(id);
        LOG.info("Courier {} {} requested  his/her order history", courierBasicInfoDTO.getFirstName(), courierBasicInfoDTO.getLastName());
        List<CourierOrderInfoDTO> courierOrderInfoDTOS  = courierService.getAllOrdersOfCourier(id);
        if (courierOrderInfoDTOS == null || courierOrderInfoDTOS.isEmpty()){
            LOG.info("No closed orders found for courier {} {}", courierBasicInfoDTO.getFirstName(), courierBasicInfoDTO.getLastName());
            throw new NoContent();
        }
        String courierOrderInfoJson = mapper.writeValueAsString(courierOrderInfoDTOS);
        return new ResponseEntity<String>(courierOrderInfoJson, HttpStatus.OK);
    }

    @Secured({"ROLE_COURIER"})
    @RequestMapping(value = "/status", method = RequestMethod.PUT)
    public ResponseEntity<Void> changeOwnStatus() {
        long id = courierService.getCurrentCourierId();
        CourierBasicInfoDTO courierBasicInfo = courierService.getCourierBasicInfo(id);
        String status = courierService.changeCourierStatus(id);
        LOG.info("{} {} changed his/her status to {}", courierBasicInfo.getFirstName(), courierBasicInfo.getLastName(), status);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @SneakyThrows
    @Secured({"ROLE_COURIER"})
    @RequestMapping(value = "/orders", method = RequestMethod.PUT)
    public ResponseEntity<String> getNewOrder() {
        long id = courierService.getCurrentCourierId();
        CourierBasicInfoDTO courierBasicInfo = courierService.getCourierBasicInfo(id);
        if (!courierService.isCourierActiveNow(id)) {
            LOG.info("Error. Courier {} {} is inactive now", courierBasicInfo.getFirstName(), courierBasicInfo.getLastName());
            throw new BadRequest();
        }
        long orderId = courierService.assignNewOrdersToCourier(id);
        if (orderId == 0){
            LOG.info("There are no available orders right now or courier already has an order");
            throw new NotFound();
        }
        LOG.info("Order with {} assigned to courier {} {}", orderId, courierBasicInfo.getFirstName(), courierBasicInfo.getLastName());
        CourierCurrentOrderInfoDTO currentOrderInfoDTO = courierService.getCurrentOrderForCourier(id);
        String currentOrderInfoJson = mapper.writeValueAsString(currentOrderInfoDTO);
        return new ResponseEntity<String>(currentOrderInfoJson, HttpStatus.OK);
    }

    @SneakyThrows
    @Secured({"ROLE_COURIER"})
    @RequestMapping(value = "/closures", method = RequestMethod.PUT)
    public ResponseEntity<String> closeOrder() {
        long id = courierService.getCurrentCourierId();
        CourierBasicInfoDTO courierBasicInfo = courierService.getCourierBasicInfo(id);
        OrderStatusInfoDTO orderStatusInfoDTO = orderService.getOrderStatusInfo(id);
        if (!orderService.isOrderConfirmedByClient(orderStatusInfoDTO.getId())) {
            LOG.info("Courier {} {} cannot close the order, the user has not confirmed its fulfillment", courierBasicInfo.getFirstName(), courierBasicInfo.getLastName());
            throw new BadRequest();
        }
        OrderClosingResponseDTO orderClosingResponseDTO = orderService.closeOrderForCourier(id);
        LOG.info("Courier {} {} closed order with id {} in time {}", courierBasicInfo.getFirstName(), courierBasicInfo.getLastName(), orderStatusInfoDTO.getId(), orderClosingResponseDTO.getExecutionTime());
        String orderClosingResponseJson = mapper.writeValueAsString(orderClosingResponseDTO);
        return new ResponseEntity<String>(orderClosingResponseJson, HttpStatus.OK);
    }

}
