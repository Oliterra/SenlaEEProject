package edu.senla.service;

import edu.senla.model.dto.*;

import java.util.List;

public interface UserService {

    List<ClientMainInfoDTO> getAllClients(int pages);

    List<AdminInfoDTO> getAllAdmins(int pages);

    List<ClientOrderInfoDTO> getAllOrdersOfClient(long clientId);

    void grantAdministratorRights(long id);

    void revokeAdministratorRights(long id);

    void createClient(String registrationRequestJson);

    ClientMainInfoDTO getClient(long id);

    ClientFullInfoDTO getClientByUsernameAndPassword(String authRequestJson);

    long getCurrentClientId();

    void updateClient(long id, String updatedClientJson);

    void deleteClient(long id);
}

