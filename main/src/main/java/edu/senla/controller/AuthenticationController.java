package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.AuthenticationControllerInterface;
import edu.senla.dto.*;
import edu.senla.security.JwtProvider;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationControllerInterface {

    private final ClientServiceInterface clientService;

    private final CourierServiceInterface courierService;

    private final JwtProvider jwtProvider;

    private final ObjectMapper mapper;

    @SneakyThrows
    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public ResponseEntity<String> authenticateClient(@RequestBody String authRequestJson) {
        AuthRequestDTO authRequestDTO = mapper.readValue(authRequestJson, AuthRequestDTO.class);
        ClientFullInfoDTO clientDTO = clientService.getClientByUsernameAndPassword(authRequestDTO.getUsername(), authRequestDTO.getPassword());
        String token = jwtProvider.generateToken(clientDTO.getUsername());
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(token);
        return new ResponseEntity<String>(mapper.writeValueAsString(authResponseDTO), HttpStatus.OK);
    }

    @SneakyThrows
    @RequestMapping(value = "/couriers", method = RequestMethod.GET)
    public ResponseEntity<String> authenticateCourier(@RequestBody String authRequestCourierJson) {
        CourierAuthRequestDTO courierAuthRequestDTO = mapper.readValue(authRequestCourierJson, CourierAuthRequestDTO.class);
        CourierFullInfoDTO courierDTO = courierService.getCourierByPhoneAndPassword(courierAuthRequestDTO.getPhone(), courierAuthRequestDTO.getPassword());
        String token = jwtProvider.generateToken(courierDTO.getPhone());
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(token);
        return new ResponseEntity<String>(mapper.writeValueAsString(authResponseDTO), HttpStatus.OK);
    }

}



