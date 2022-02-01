package edu.senla.controller.controllerinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface StaffControllerInterface {

    public ResponseEntity<String> getCouriersPerformanceIndicator(@PathVariable long id);

    public ResponseEntity<Void> assignOrdersToAllCouriers();

}
