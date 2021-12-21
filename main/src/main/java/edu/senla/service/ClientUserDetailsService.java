package edu.senla.service;

import edu.senla.dao.daointerface.ClientRepositoryInterface;
import edu.senla.entity.Client;
import edu.senla.security.ClientUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;

@Service
@RequiredArgsConstructor
public class ClientUserDetailsService implements UserDetailsService {

    private final ClientRepositoryInterface clientRepository;

    @Override
    public ClientUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = getClientByUsername(username);
        return ClientUserDetails.fromClientEntityToClientUserDetails(client);
    }

    public Client getClientByUsername(String username) {
        try {
            return clientRepository.getClientByUsername(username);
        }
        catch (NoResultException e){
            return null;
        }
    }

}
