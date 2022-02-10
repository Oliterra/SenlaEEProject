package edu.senla.controller;

import edu.senla.model.dto.AdminInfoDTO;

import java.util.List;

public interface AdministratorController {

    List<AdminInfoDTO> getAllAdmins(int pages);

    void grantAdministratorRights(long id);

    void takeAwayAdministratorRights(long id);
}
