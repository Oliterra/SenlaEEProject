package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.AdministratorControllerInterface;
import edu.senla.dto.AdminInfoDTO;
import edu.senla.dto.ClientRoleInfoDTO;
import edu.senla.exeptions.BadRequest;
import edu.senla.exeptions.NoContent;
import edu.senla.exeptions.NotFound;
import edu.senla.service.serviceinterface.ClientServiceInterface;
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
@RequestMapping("/administrators")
public class AdministratorController implements AdministratorControllerInterface {

    private final ClientServiceInterface clientService;

    private final ObjectMapper mapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(AdministratorController.class);

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getAllAdmins() {
        LOG.info("Getting all users with the administrator role");
        List<AdminInfoDTO> adminInfoDTOS = clientService.getAllAdmins();
        if (adminInfoDTOS.isEmpty()){
            LOG.info("No admins found");
            throw new NoContent();
        }
        String adminInfoJson = mapper.writeValueAsString(adminInfoDTOS);
        return new ResponseEntity<String>(adminInfoJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "/assigning/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> grantAdministratorRights(@PathVariable long id) {
        ClientRoleInfoDTO clientRoleInfoDTO = clientService.getClientRole(id);
        if (clientRoleInfoDTO == null) {
            LOG.info("A user with id {} not found", id);
            throw new NotFound();
        }
        if (clientService.isUserAdmin(clientRoleInfoDTO)){
            LOG.info("Error. User {} {} is already an admin", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName());
            throw new BadRequest();
        }
        LOG.info("Endowment with administrator rights of the user {} {} (current role: {})", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName(), clientRoleInfoDTO.getRole());
        clientService.grantAdministratorRights(id);
        LOG.info("User {} {} is an administrator now", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "/deprivation/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> takeAwayAdministratorRights(@PathVariable long id) {
        ClientRoleInfoDTO clientRoleInfoDTO = clientService.getClientRole(id);
        if (clientRoleInfoDTO == null) {
            LOG.info("A user with id {} not found", id);
            throw new NotFound();
        }
        if (!clientService.isUserAdmin(clientRoleInfoDTO)){
            LOG.info("Error. User {} {} was not an admin", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName());
            throw new BadRequest();
        }
        LOG.info("Depriving the user {} {} of administrator rights", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName());
        clientService.revokeAdministratorRights(id);
        LOG.info("User {} {} is a user now", clientRoleInfoDTO.getFirstName(), clientRoleInfoDTO.getLastName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
