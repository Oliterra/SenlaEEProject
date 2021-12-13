package edu.senla.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.dto.AuthRequestDTO;
import edu.senla.dto.AuthResponseDTO;
import edu.senla.dto.ClientDTO;
import edu.senla.security.JwtProvider;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(AuthenticationController.class);

    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST)
    public AuthResponseDTO authenticate(@RequestBody String authRequestJson) {
        AuthRequestDTO authRequestDTO = jacksonObjectMapper.readValue(authRequestJson, AuthRequestDTO.class);
        ClientDTO clientDTO = clientService.getByUsernameAndPassword(authRequestDTO.getUsername(), authRequestDTO.getPassword());

        String token = jwtProvider.generateToken(clientDTO.getUsername());

        AuthResponseDTO authResponseDTO = new AuthResponseDTO(token);
        LOG.info("JWT: " + authResponseDTO);

        return authResponseDTO;
    }

}



