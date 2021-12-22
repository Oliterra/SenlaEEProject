package edu.senla.service;

import edu.senla.dao.ClientRepositoryInterface;
import edu.senla.dao.RoleRepositoryInterface;
import edu.senla.dto.ClientDTO;
import edu.senla.entity.Client;
import edu.senla.entity.Role;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ClientService implements ClientServiceInterface {

    private final ClientRepositoryInterface clientRepository;

    private final RoleRepositoryInterface roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper mapper;

    @Override
    public ClientDTO createClient(ClientDTO newClientDTO) {
        Client newClient = clientRepository.save(mapper.map(newClientDTO, Client.class));
        Role clientRole = roleRepository.getRoleByName("ROLE_USER");
        newClient.setRole(clientRole);
        newClient.setPassword(passwordEncoder.encode(newClient.getPassword()));
        return mapper.map(newClient, ClientDTO.class);
    }

    @Override
    public ClientDTO readClient(long id) {
        return mapper.map(clientRepository.getById(id), ClientDTO.class);
    }

    @Override
    public ClientDTO updateClient(long id, ClientDTO clientDTO) {
        Client updatedClient = mapper.map(clientDTO, Client.class);
        Client clientToUpdate = clientRepository.getById(id);

        Client clientWithNewParameters = updateClientsOptions(clientToUpdate, updatedClient);
        Client client = clientRepository.save(clientWithNewParameters);

        return mapper.map(client, ClientDTO.class);
    }

    private Client updateClientsOptions(Client client, Client updatedClient)
    {
        client.setFirstName(updatedClient.getFirstName());
        client.setLastName(updatedClient.getLastName());
        client.setEmail(updatedClient.getEmail());
        client.setPhone(updatedClient.getPhone());
        client.setAddress(updatedClient.getAddress());
        return client;
    }

    @Override
    public void deleteClient(long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public ClientDTO getByIdWithOrders(long clientId) {
        //return mapper.map(clientRepository.getByIdWithOrders(clientId), ClientDTO.class);
        return null;
    }

    @Override
    public boolean isClientExists(ClientDTO client) {
        try {
            return clientRepository.getClientByEmail(client.getEmail()) != null;
        }
        catch (NoResultException e){
            return false;
        }
    }

    @Override
    public ClientDTO getClientByUsernameAndPassword(String username, String password) {
        try {
            Client client = clientRepository.getClientByUsername(username);
            if (passwordEncoder.matches(password, client.getPassword())) {
                return mapper.map(client, ClientDTO.class);
            }
        }
        catch (NoResultException e){

        }
        return null;
    }

}

