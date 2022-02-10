package edu.senla.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.ClientController;
import edu.senla.model.dto.ClientMainInfoDTO;
import edu.senla.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientControllerImpl implements ClientController {

    private final ClientService clientService;
    private final ObjectMapper mapper;

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
    @SneakyThrows
    @PutMapping(value = "{id}")
    public void updateClient(@PathVariable long id, @RequestBody String updatedClientJson) {
        clientService.updateClient(id, mapper.readValue(updatedClientJson, ClientMainInfoDTO.class));
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "{id}")
    public void deleteClient(@PathVariable("id") long id) {
        clientService.deleteClient(id);
    }
}

