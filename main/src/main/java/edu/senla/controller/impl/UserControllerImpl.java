package edu.senla.controller.impl;

import edu.senla.controller.UserController;
import edu.senla.model.dto.UserMainInfoDTO;
import edu.senla.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Secured({"ROLE_ADMIN"})
    @GetMapping
    public List<UserMainInfoDTO> getAllClients(@RequestParam(value = "pages", required = false, defaultValue = "10") int pages) {
        return userService.getAllClients(pages);
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "{id}")
    public UserMainInfoDTO getClient(@PathVariable("id") long id) {
        return userService.getClient(id);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "{id}")
    public void updateClient(@PathVariable long id, @RequestBody String updatedClientJson) {
        userService.updateClient(id, updatedClientJson);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "{id}")
    public void deleteClient(@PathVariable("id") long id) {
        userService.deleteClient(id);
    }
}

