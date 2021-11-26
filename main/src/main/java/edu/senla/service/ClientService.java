package edu.senla.service;

import edu.senla.dao.daointerface.ClientRepositoryInterface;
import edu.senla.dto.ClientDTO;
import edu.senla.entity.Client;
import edu.senla.entity.Courier;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ClientService implements ClientServiceInterface{

    private final ClientRepositoryInterface clientRepository;

    private final ModelMapper mapper;

    @Override
    public void createClient(ClientDTO newClientDTO) {
        clientRepository.create(mapper.map(newClientDTO, Client.class));
    }

    @Override
    public ClientDTO readClient(int id) {
        Client requestedСlient = clientRepository.read(id);
        return mapper.map(requestedСlient, ClientDTO.class);
    }

    @Override
    public void updateClient(int id, ClientDTO updatedClientDTO) {
        Client updatedClient = mapper.map(updatedClientDTO, Client.class);
        Client clientToUpdate = mapper.map(readClient(id), Client.class);
        Client clientWithNewParameters = updateClientsOptions(clientToUpdate, updatedClient);
        clientRepository.update(clientWithNewParameters);
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
    public int getClientIdByEmail(String clientEmail){
        return clientRepository.getIdByEmail(clientEmail);
    }

    @Override
    public String getByIdWithOrders(int clientId) {
        return clientRepository.getByIdWithOrders(clientId).toString();
    }

    @Override
    public String getByIdWithOrdersJPQL(int clientId) {
        return clientRepository.getByIdWithOrdersJPQL(clientId).toString();
    }

}
