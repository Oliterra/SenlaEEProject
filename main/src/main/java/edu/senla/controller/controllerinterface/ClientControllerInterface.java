package edu.senla.controller.controllerinterface;

public interface ClientControllerInterface {

    public void createClient(String newClientJson);

    public String readClient(int id);

    public void updateClient(int id, String updatedClientJson);

    public void deleteClient(int id);

}
