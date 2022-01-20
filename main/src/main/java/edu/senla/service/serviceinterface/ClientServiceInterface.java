package edu.senla.service.serviceinterface;

import edu.senla.dto.*;

import java.util.List;

public interface ClientServiceInterface {

    List<ClientMainInfoDTO> getAllClients();

    List<AdminInfoDTO> getAllAdmins();

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

