package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.CourierControllerInterface;
import edu.senla.dto.CourierMainInfoDTO;
import edu.senla.exeptions.ConflictBetweenData;
import edu.senla.exeptions.NoContent;
import edu.senla.exeptions.NotFound;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/couriers")
public class CourierController implements CourierControllerInterface {

    private final CourierServiceInterface courierService;

    private final ObjectMapper mapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(CourierController.class);

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getAllCouriers() {
        LOG.info("Getting all couriers");
        List<CourierMainInfoDTO> courierMainInfoDTOs = courierService.getAllCouriers();
        if (courierMainInfoDTOs == null || courierMainInfoDTOs.isEmpty()){
            LOG.info("No couriers found");
            throw new NoContent();
        }
        String courierMainInfoJson = mapper.writeValueAsString(courierMainInfoDTOs);
        return new ResponseEntity<String>(courierMainInfoJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getCourier(@PathVariable("id") long id) {
        LOG.info("Getting courier with id: {}", id);
        if (!courierService.isCourierExists(id)) {
            LOG.info("There is no courier with id {}", id);
            throw new NotFound();
        }
        CourierMainInfoDTO courierMainInfoDTO = courierService.getCourier(id);
        String courierMainInfoJson = mapper.writeValueAsString(courierMainInfoDTO);
        return new ResponseEntity<String>(courierMainInfoJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateCourier(@PathVariable long id, @RequestBody String updatedCourierJson) {
        LOG.info("Updating courier with id {} with new data {}: ", id, updatedCourierJson);
        CourierMainInfoDTO updatedDishDTO = mapper.readValue(updatedCourierJson, CourierMainInfoDTO.class);
        if (!courierService.isCourierExists(id)) {
            LOG.info("There is no courier with id {}", id);
            throw new NotFound();
        }
        if (courierService.isCourierExists(updatedDishDTO.getPhone())) {
            LOG.info("Courier with phone {} already exists", updatedDishDTO.getPhone());
            throw new ConflictBetweenData();
        }
        courierService.updateCourier(id, updatedDishDTO);
        LOG.info("Courier with id {} successfully updated", id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCourier(@PathVariable("id") long id) {
        LOG.info("Deleting courier with id: {}", id);
        if (!courierService.isCourierExists(id)) {
            LOG.info("There is no courier with id {}", id);
            throw new NotFound();
        }
        courierService.deleteCourier(id);
        LOG.info("Courier with id {} successfully deleted", id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
