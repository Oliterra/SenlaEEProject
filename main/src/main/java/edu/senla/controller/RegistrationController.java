package edu.senla.controller;

public interface RegistrationController {

    void registerClient(String registrationRequestJson);

    void registerCourier(String courierRegistrationRequestJson);
}
