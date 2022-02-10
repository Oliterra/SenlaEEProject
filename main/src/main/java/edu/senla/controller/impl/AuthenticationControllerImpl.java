package edu.senla.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.AuthenticationController;
import edu.senla.security.JwtProvider;
import edu.senla.service.ClientService;
import edu.senla.service.CourierService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import edu.senla.model.dto.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements AuthenticationController {

    private final ClientService clientService;
    private final CourierService courierService;
    private final JwtProvider jwtProvider;
    private final ObjectMapper mapper;

    @SneakyThrows
    @GetMapping(value = "/clients")
    public AuthResponseDTO authenticateClient(@RequestBody String authRequestJson) {
        AuthRequestDTO authRequestDTO = mapper.readValue(authRequestJson, AuthRequestDTO.class);
        ClientFullInfoDTO clientDTO = clientService.getClientByUsernameAndPassword(authRequestDTO.getUsername(), authRequestDTO.getPassword());
        String token = jwtProvider.generateToken(clientDTO.getUsername());
        return new AuthResponseDTO(token);
    }

    @SneakyThrows
    @GetMapping(value = "/couriers")
    public AuthResponseDTO authenticateCourier(@RequestBody String authRequestCourierJson) {
        CourierAuthRequestDTO courierAuthRequestDTO = mapper.readValue(authRequestCourierJson, CourierAuthRequestDTO.class);
        CourierFullInfoDTO courierDTO = courierService.getCourierByPhoneAndPassword(courierAuthRequestDTO.getPhone(), courierAuthRequestDTO.getPassword());
        String token = jwtProvider.generateToken(courierDTO.getPhone());
        return new AuthResponseDTO(token);
    }
}



