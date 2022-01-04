package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.OrdersHistoryControllerInterface;
import edu.senla.dto.ClientBasicInfoDTO;
import edu.senla.dto.ClientOrderInfoDTO;
import edu.senla.dto.CourierBasicInfoDTO;
import edu.senla.dto.CourierOrderInfoDTO;
import edu.senla.exeptions.NoContent;
import edu.senla.exeptions.NotFound;
import edu.senla.service.serviceinterface.ClientServiceInterface;
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
@RequestMapping("/history")
public class OrdersHistoryController implements OrdersHistoryControllerInterface {

    private final ClientServiceInterface clientService;

    private final CourierServiceInterface courierService;

    private final ObjectMapper mapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(OrdersHistoryController.class);

    @SneakyThrows
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "clients/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getClientOrdersHistory(@PathVariable long id) {
        ClientBasicInfoDTO clientBasicInfo = clientService.getClientBasicInfo(id);
        if (clientBasicInfo == null) {
            LOG.info("Client with id {} not found", id);
            throw new NotFound();
        }
        LOG.info("Requested order history for the client {} {}", clientBasicInfo.getFirstName(), clientBasicInfo.getLastName());
        List<ClientOrderInfoDTO> clientOrderInfoDTOs  = clientService.getAllOrdersOfClient(id);
        if (clientOrderInfoDTOs == null || clientOrderInfoDTOs.isEmpty()){
            LOG.info("No orders found for client {} {}", clientBasicInfo.getFirstName(), clientBasicInfo.getLastName());
            throw new NoContent();
        }
        String clientOrderInfoJson = mapper.writeValueAsString(clientOrderInfoDTOs);
        return new ResponseEntity<String>(clientOrderInfoJson, HttpStatus.OK);
    }

    @SneakyThrows
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "orders/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getCouriersOrdersHistory(@PathVariable long id) {
        CourierBasicInfoDTO courierBasicInfoDTO = courierService.getCourierBasicInfo(id);
        if (courierBasicInfoDTO == null) {
            LOG.info("Courier with id {} not found", id);
            throw new NotFound();
        }
        LOG.info("Requested order history for the courier {} {}", courierBasicInfoDTO.getFirstName(), courierBasicInfoDTO.getLastName());
        List<CourierOrderInfoDTO> courierOrderInfoDTOs  = courierService.getAllOrdersOfCourier(id);
        if (courierOrderInfoDTOs == null || courierOrderInfoDTOs.isEmpty()){
            LOG.info("No closed orders found for courier {} {}", courierBasicInfoDTO.getFirstName(), courierBasicInfoDTO.getLastName());
            throw new NoContent();
        }
        String courierOrderInfoJson = mapper.writeValueAsString(courierOrderInfoDTOs);
        return new ResponseEntity<String>(courierOrderInfoJson, HttpStatus.OK);
    }

}
