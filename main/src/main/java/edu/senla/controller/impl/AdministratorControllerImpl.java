package edu.senla.controller.impl;

import edu.senla.controller.AdministratorController;
import edu.senla.model.dto.AdminInfoDTO;
import edu.senla.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/administrators")
public class AdministratorControllerImpl implements AdministratorController {

    private final UserService userService;

    @Secured({"ROLE_ADMIN"})
    @GetMapping
    public List<AdminInfoDTO> getAllAdmins(@RequestParam(value = "pages", required = false, defaultValue = "10") int pages) {
        return userService.getAllAdmins(pages);
    }

    @Secured({"ROLE_ADMIN"})
    @PatchMapping(value = "/assigning/{id}")
    public void grantAdministratorRights(@PathVariable long id) {
        userService.grantAdministratorRights(id);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "/deprivation/{id}")
    public void takeAwayAdministratorRights(@PathVariable long id) {
        userService.revokeAdministratorRights(id);
    }
}
