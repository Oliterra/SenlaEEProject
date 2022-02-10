package edu.senla.controller;

import edu.senla.model.dto.AuthResponseDTO;

public interface AuthenticationController {

    AuthResponseDTO authenticateClient(String authRequestJson);

    AuthResponseDTO authenticateCourier(String authRequestCourierJson);
}
