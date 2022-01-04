package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface AdministratorControllerInterface {

    public ResponseEntity<String> getAllAdmins();

    public ResponseEntity<Void> grantAdministratorRights(@PathVariable long id);

    public ResponseEntity<Void> takeAwayAdministratorRights(@PathVariable long id);

}
