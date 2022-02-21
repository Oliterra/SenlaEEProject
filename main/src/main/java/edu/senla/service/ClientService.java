package edu.senla.service;

import edu.senla.model.dto.*;

import java.util.List;

public interface ClientService {

    List<UserMainInfoDTO> getAllClients(int pages);

    List<AdminInfoDTO> getAllAdmins(int pages);

    List<UserOrderInfoDTO> getAllOrdersOfClient(long clientId);

    void grantAdministratorRights(long id);

    void revokeAdministratorRights(long id);

    void createClient(String registrationRequestJson);

    UserMainInfoDTO getClient(long id);

    UserFullInfoDTO getClientByUsernameAndPassword(String authRequestJson);

    long getCurrentClientId();

    void updateClient(long id, String updatedClientJson);

    void deleteClient(long id);
}

