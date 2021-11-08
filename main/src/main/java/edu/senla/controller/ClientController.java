package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.dto.ClientDTO;
import edu.senla.service.ClientServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientController implements ClientControllerInterface{

    private final ClientServiceInterface clientService;

    private final ObjectMapper jacksonObjectMapper;

    @SneakyThrows
    @Override
    public void createClient(String newClientJson) {
        ClientDTO newClientDTO = jacksonObjectMapper.readValue(newClientJson, ClientDTO.class);
        clientService.createClient(newClientDTO);
        System.out.println("Client" + read(newClientDTO) + " was successfully created");
    }

    @SneakyThrows
    @Override
    public String read(ClientDTO clientToReadDTO) {
        ClientDTO clientDTO = clientService.read(clientToReadDTO.getId());
        return jacksonObjectMapper.writeValueAsString(clientDTO);
    }

    @SneakyThrows
    @Override
    public void update(ClientDTO clientToUpdateDTO, String updatedClientJson) {
        ClientDTO updatedClientDTO = jacksonObjectMapper.readValue(updatedClientJson, ClientDTO.class);
        clientService.update(clientToUpdateDTO, updatedClientDTO);
        System.out.println("Client was successfully updated");
    }

    @Override
    public void delete(ClientDTO clientToDeleteDTO) {
        clientService.delete(clientToDeleteDTO.getId());
        System.out.println("Client was successfully deleted");
    }

}
