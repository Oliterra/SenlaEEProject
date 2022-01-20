package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.AdministratorControllerInterface;
import edu.senla.dto.AdminInfoDTO;
import edu.senla.service.serviceinterface.ClientServiceInterface;
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
@RequestMapping("/administrators")
public class AdministratorController implements AdministratorControllerInterface {

    private final ClientServiceInterface clientService;

    private final ObjectMapper mapper;

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getAllAdmins() {
        List<AdminInfoDTO> adminInfoDTOS = clientService.getAllAdmins();
        String adminInfoJson = mapper.writeValueAsString(adminInfoDTOS);
        return new ResponseEntity<String>(adminInfoJson, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "/assigning/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> grantAdministratorRights(@PathVariable long id) {
        clientService.grantAdministratorRights(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @SneakyThrows
    @RequestMapping(value = "/deprivation/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> takeAwayAdministratorRights(@PathVariable long id) {
        clientService.revokeAdministratorRights(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
