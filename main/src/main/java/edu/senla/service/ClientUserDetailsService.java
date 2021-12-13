package edu.senla.service;

import edu.senla.entity.Client;
import edu.senla.security.ClientUserDetails;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClientUserDetailsService implements UserDetailsService {

    @Autowired
    private ClientServiceInterface clientService;

    @Override
    public ClientUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientService.getByUsername(username);
        return ClientUserDetails.fromClientEntityToClientUserDetails(client);
    }

}
