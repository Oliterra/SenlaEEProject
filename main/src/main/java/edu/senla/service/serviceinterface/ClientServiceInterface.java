package edu.senla.service.serviceinterface;

import edu.senla.dto.*;

import java.util.List;

public interface ClientServiceInterface {

    List<ClientMainInfoDTO> getAllClients();

    List<AdminInfoDTO> getAllAdmins();

    List<ClientOrderInfoDTO> getAllOrdersOfClient(long clientId);

    void createClient(RegistrationRequestDTO newClientDTO);

    ClientMainInfoDTO getClient(long id);

    ClientFullInfoDTO getClientByUsernameAndPassword(String username, String password);

    ClientBasicInfoDTO getClientBasicInfo(long id);

    ClientRoleInfoDTO getClientRole(long id);

    long getCurrentClientId();

    void updateClient(long id, ClientMainInfoDTO updatedClientDTO);

    void deleteClient(long id);

    String isClientExists(RegistrationRequestDTO registrationRequestDTO);

    String isClientExists(ClientMainInfoDTO clientMainInfoDTO);

    boolean isClientExists(long id);

    boolean isUserAdmin(ClientRoleInfoDTO clientRoleInfoDTO);

    void grantAdministratorRights(long id);

    void revokeAdministratorRights(long id);

}

