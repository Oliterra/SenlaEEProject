package edu.senla.service;

import edu.senla.dao.daointerface.ClientRepositoryInterface;
import edu.senla.dto.ClientDTO;
import edu.senla.entity.Client;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ClientService implements ClientServiceInterface{

    private final ClientRepositoryInterface clientRepository;

    private final ModelMapper mapper;

    @Override
    public ClientDTO createClient(ClientDTO newClientDTO) {
        Client newClient = clientRepository.create(mapper.map(newClientDTO, Client.class));
        return mapper.map(newClient, ClientDTO.class);
    }

    @Override
    public ClientDTO readClient(int id) {
        Client requestedСlient = clientRepository.read(id);
        return mapper.map(requestedСlient, ClientDTO.class);
    }

    @Override
    public ClientDTO updateClient(int id, ClientDTO clientDTO) {
        Client updatedClient = mapper.map(clientDTO, Client.class);
        Client clientToUpdate = mapper.map(readClient(id), Client.class);

        Client clientWithNewParameters = updateClientsOptions(clientToUpdate, updatedClient);
        Client client = clientRepository.update(clientWithNewParameters);

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
    public void deleteClient(int id) {
        clientRepository.delete(id);
    }

    @Override
    public ClientDTO getByIdWithOrders(int clientId) {
        return mapper.map(clientRepository.getByIdWithOrders(clientId), ClientDTO.class);
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

}

