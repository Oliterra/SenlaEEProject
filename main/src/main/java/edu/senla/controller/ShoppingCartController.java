package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.ShoppingCartControllerInterface;
import edu.senla.dto.*;
import edu.senla.exeptions.BadRequest;
import edu.senla.exeptions.NoContent;
import edu.senla.exeptions.NotFound;
import edu.senla.service.serviceinterface.*;
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
@RequestMapping("/shoppingCart")
public class ShoppingCartController implements ShoppingCartControllerInterface {

    private final ClientServiceInterface clientService;

    private final ContainerServiceInterface containerService;

    private final DishServiceInterface dishService;

    private final OrderServiceInterface orderService;

    private final ObjectMapper mapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(ShoppingCartController.class);

    @SneakyThrows
    @Secured({"ROLE_USER"})
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity<String> getOrdersHistory() {
        long id = clientService.getCurrentClientId();
        ClientBasicInfoDTO clientBasicInfo = clientService.getClientBasicInfo(id);
        LOG.info("Client {} {} requested  his/her order history", clientBasicInfo.getFirstName(), clientBasicInfo.getLastName());
        List<ClientOrderInfoDTO> clientOrderInfoDTOs  = clientService.getAllOrdersOfClient(id);
        if (clientOrderInfoDTOs == null || clientOrderInfoDTOs.isEmpty()){
            LOG.info("No closed orders found for client {} {}", clientBasicInfo.getFirstName(), clientBasicInfo.getLastName());
            throw new NoContent();
        }
        String clientOrderInfoJson = mapper.writeValueAsString(clientOrderInfoDTOs);
        return new ResponseEntity<String>(clientOrderInfoJson, HttpStatus.OK);
    }

    @SneakyThrows
    @Secured({"ROLE_USER"})
    @RequestMapping(value = "/containerParams", method = RequestMethod.GET)
    public ResponseEntity<String> getWeightOfProductsInContainer(@RequestBody String containerComponentsJson) {
        ContainerComponentsDTO containerComponentsDTO = mapper.readValue(containerComponentsJson, ContainerComponentsDTO.class);
        if (!containerService.isContainerComponentsCorrect(containerComponentsDTO)) {
            LOG.info("There is no such type of container or non-existent dish found in container");
            throw new NotFound();
        }
        if (!dishService.isAllDishesHaveDishInformation(containerComponentsDTO)) {
            LOG.info("There is not enough information about the dishes to calculate");
            throw new BadRequest();
        }
        ContainerComponentsParamsDTO containerComponentsParamsDTO = containerService.calculateWeightOfDishes(containerComponentsDTO);
        LOG.info("Calculated the weight of the dishes for the container size {} : {}", containerComponentsDTO.getTypeOfContainer(), containerComponentsParamsDTO);
        String containerComponentsWeightJson = mapper.writeValueAsString(containerComponentsParamsDTO);
        return new ResponseEntity<String>(containerComponentsWeightJson, HttpStatus.OK);
    }

    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST)
    @Secured({"ROLE_USER"})
    public ResponseEntity<String> makeOrder(@RequestBody String shoppingCartJson) {
        ShoppingCartDTO shoppingCartDTO = mapper.readValue(shoppingCartJson, ShoppingCartDTO.class);
        if (!containerService.isPaymentTypeCorrect(shoppingCartDTO.getPaymentType())){
            LOG.info("Incorrect payment type");
            throw new BadRequest();
        }
        List correctContainers = containerService.filterContainers(shoppingCartDTO.getContainers());
        if (correctContainers.isEmpty()){
            LOG.info("There is no items in shopping cart");
            throw new BadRequest();
        }
        shoppingCartDTO.setContainers(correctContainers);
        long clientId = clientService.getCurrentClientId();
        OrderTotalCostDTO totalCost = containerService.makeOrder(clientId, shoppingCartDTO);
        String totalCostJson = mapper.writeValueAsString(totalCost);
        return new ResponseEntity<String>(totalCostJson, HttpStatus.OK);
    }

    @SneakyThrows
    @Secured({"ROLE_USER"})
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> confirmReceiptOfTheOrder(@PathVariable long id) {
        long clientId = clientService.getCurrentClientId();
        if (!orderService.isOrderBelongToClient(id, clientId)) {
            LOG.info("The order {} does not belong to the client {}", id, clientId);
            throw new BadRequest();
        }
        if (!orderService.isOrderIsInProcess(id)) {
            LOG.info("The order {} cannot be closed by the client, incorrect status", id);
            throw new BadRequest();
        }
        orderService.closeOrderForClient(id);
        LOG.info("The order receipt {} is confirmed", id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
