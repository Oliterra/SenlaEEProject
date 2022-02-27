package edu.senla.controller.impl;

import edu.senla.controller.RandomDatabaseFillingController;
import edu.senla.service.impl.RandomDatabaseFillingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class RandomDatabaseFillingControllerImpl implements RandomDatabaseFillingController {

    private final RandomDatabaseFillingServiceImpl randomDatabaseFillingService;

    @PostMapping(value = "/users")
    public void createCertainNumberOfUsers(@RequestParam(value = "number", required = false, defaultValue = "20") int number) {
        randomDatabaseFillingService.createCertainNumberOfUsers(number);
    }

    @PostMapping(value = "/couriers")
    public void createCertainNumberOfCouriers(@RequestParam(value = "number", required = false, defaultValue = "20") int number) {
        randomDatabaseFillingService.createCertainNumberOfCouriers(number);
    }
}
