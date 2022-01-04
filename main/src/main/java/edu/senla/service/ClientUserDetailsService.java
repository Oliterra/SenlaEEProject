package edu.senla.service;

import edu.senla.dao.ClientRepositoryInterface;
import edu.senla.dao.CourierRepositoryInterface;
import edu.senla.entity.Client;
import edu.senla.entity.Courier;
import edu.senla.security.ClientUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class ClientUserDetailsService implements UserDetailsService {

    private final ClientRepositoryInterface clientRepository;

    private final CourierRepositoryInterface courierRepository;

    private final EntityManager entityManager;

    @Override
    public ClientUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.startsWith("+375")) {
            Courier courier = courierRepository.getByPhone(username);
            return ClientUserDetails.fromCourierEntityToCourierUserDetails(courier);
        } else {
            Client client = clientRepository.getByUsername(username);
            return ClientUserDetails.fromClientEntityToClientUserDetails(client);
        }
    }

}
