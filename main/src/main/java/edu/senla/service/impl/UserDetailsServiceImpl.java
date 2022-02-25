package edu.senla.service.impl;

import edu.senla.dao.ClientRepository;
import edu.senla.dao.CourierRepository;
import edu.senla.model.entity.Client;
import edu.senla.model.entity.Courier;
import edu.senla.security.ClientUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final CourierRepository courierRepository;

    @Override
    public ClientUserDetails loadUserByUsername(String username) {
        if (username.startsWith("+375")) {
            Courier courier = courierRepository.getByPhone(username);
            return ClientUserDetails.fromCourierEntityToCourierUserDetails(courier);
        } else {
            Client client = clientRepository.getByUsername(username);
            return ClientUserDetails.fromClientEntityToClientUserDetails(client);
        }
    }
}
