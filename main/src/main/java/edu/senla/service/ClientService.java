package edu.senla.service;

import edu.senla.annotation.Transaction;
import edu.senla.dao.ClientJDBC;
import edu.senla.dto.ClientDTO;
import edu.senla.entity.Client;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService implements ClientServiceInterface {

    private final ClientJDBC clientDAO;

    private final ModelMapper mapper;

    @Override
    @Transaction
    public void createClient(ClientDTO newClientDTO) {
        clientDAO.create(mapper.map(newClientDTO, Client.class));
    }

    @Override
    @Transaction
    public ClientDTO read(int id) {
        Client requestedСlient = clientDAO.read(id);
        return mapper.map(requestedСlient, ClientDTO.class);
    }

    @Override
    @Transaction
    public void update(int id, ClientDTO updatedClientDTO) {
        Client updatedClient = mapper.map(updatedClientDTO, Client.class);
        clientDAO.update(id, updatedClient);
    }

    @Override
    @Transaction
    public void delete(int id) {
        clientDAO.delete(id);
    }
}
