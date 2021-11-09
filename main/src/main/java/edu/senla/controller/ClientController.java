package edu.senla.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.controller.controllerinterface.ClientControllerInterface;
import edu.senla.dto.ClientDTO;
import edu.senla.service.serviceinterface.ClientServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientController implements ClientControllerInterface {

    private final ClientServiceInterface clientService;

    private final ObjectMapper jacksonObjectMapper;

    @SneakyThrows
    @Override
    public void createClient(String newClientJson) {
        ClientDTO newClientDTO = jacksonObjectMapper.readValue(newClientJson, ClientDTO.class);
        clientService.createClient(newClientDTO);
        System.out.println("Client" + readClient(newClientDTO.getId()) + " was successfully created");
    }

    @SneakyThrows
    @Override
    public String readClient(int id) {
        ClientDTO clientDTO = clientService.read(id);
        return jacksonObjectMapper.writeValueAsString(clientDTO);
    }

    @SneakyThrows
    @Override
    public void updateClient(String clientToUpdateJson, String updatedClientJson) {
        ClientDTO clientToUpdateDTO = jacksonObjectMapper.readValue(clientToUpdateJson, ClientDTO.class);
        ClientDTO updatedClientDTO = jacksonObjectMapper.readValue(updatedClientJson, ClientDTO.class);
        clientService.update(clientToUpdateDTO, updatedClientDTO);
        System.out.println("Client was successfully updated");
    }

    @Override
    public void deleteClient(int id) {
        clientService.delete(id);
        System.out.println("Client was successfully deleted");
    }

}
