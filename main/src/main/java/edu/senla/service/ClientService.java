package edu.senla.service;

import edu.senla.dao.DAO;
import edu.senla.dto.ClientDTO;
import edu.senla.entity.Client;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService implements ClientServiceInterface {

    private final DAO<Client> clientDAO;

    private final ModelMapper mapper;

    @Override
    public void createClient(ClientDTO newClientDTO) {
        clientDAO.create(mapper.map(newClientDTO, Client.class));
    }

    @Override
    public ClientDTO read(int id) {
        Client requestedСlient = clientDAO.read(id);
        return mapper.map(requestedСlient, ClientDTO.class);
    }

    @Override
    public Client update(ClientDTO clientToUpdateDTO, ClientDTO updatedClientDTO) {
        Client updatedClient = mapper.map(updatedClientDTO, Client.class);
        return updateClientsOptions(clientDAO.read(clientToUpdateDTO.getId()), updatedClient);
    }

    @Override
    public void delete(int id) {
        clientDAO.delete(id);
    }

    private Client updateClientsOptions(Client client, Client updatedClient)
    {
        client.setId(updatedClient.getId());
        client.setFirstName(updatedClient.getFirstName());
        client.setLastName(updatedClient.getLastName());
        client.setEmail(updatedClient.getEmail());
        client.setPhone(updatedClient.getPhone());
        client.setAddress(updatedClient.getAddress());
        return client;
    }

}
