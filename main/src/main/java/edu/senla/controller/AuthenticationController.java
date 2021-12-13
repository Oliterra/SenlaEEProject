package edu.senla.controller;

import edu.senla.dto.AuthRequestDTO;
import edu.senla.dto.AuthResponseDTO;
import edu.senla.dto.ClientDTO;
import edu.senla.security.JwtProvider;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    @Autowired
    private ClientServiceInterface clientService;
    @Autowired
    private JwtProvider jwtProvider;

    @RequestMapping(method = RequestMethod.POST)
    public AuthResponseDTO authenticate(@RequestBody AuthRequestDTO request) {
        ClientDTO clientDTO = clientService.getByUsernameAndPassword(request.getUsername(), request.getPassword());
        String token = jwtProvider.generateToken(clientDTO.getUsername());
        return new AuthResponseDTO(token);
    }

}



