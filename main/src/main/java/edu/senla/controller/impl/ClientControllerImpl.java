package edu.senla.controller.impl;

import edu.senla.controller.ClientController;
import edu.senla.model.dto.ClientMainInfoDTO;
import edu.senla.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientControllerImpl implements ClientController {

    private final ClientService clientService;

    @Secured({"ROLE_ADMIN"})
    @GetMapping
    public List<ClientMainInfoDTO> getAllClients(@RequestParam(value = "pages", required = false, defaultValue = "10") int pages) {
        return clientService.getAllClients(pages);
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "{id}")
    public ClientMainInfoDTO getClient(@PathVariable("id") long id) {
        return clientService.getClient(id);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "{id}")
    public void updateClient(@PathVariable long id, @RequestBody String updatedClientJson) {
        clientService.updateClient(id, updatedClientJson);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "{id}")
    public void deleteClient(@PathVariable("id") long id) {
        clientService.deleteClient(id);
    }
}

