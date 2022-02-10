package edu.senla.service;

import edu.senla.model.dto.*;

import java.util.List;

public interface ClientService {

    List<ClientMainInfoDTO> getAllClients(int pages);

    List<AdminInfoDTO> getAllAdmins(int pages);

    List<ClientOrderInfoDTO> getAllOrdersOfClient(long clientId);

    void grantAdministratorRights(long id);

    void revokeAdministratorRights(long id);

    void createClient(RegistrationRequestDTO newClientDTO);

    ClientMainInfoDTO getClient(long id);

    ClientFullInfoDTO getClientByUsernameAndPassword(String username, String password);

    long getCurrentClientId();

    void updateClient(long id, ClientMainInfoDTO updatedClientDTO);

    void deleteClient(long id);
}

