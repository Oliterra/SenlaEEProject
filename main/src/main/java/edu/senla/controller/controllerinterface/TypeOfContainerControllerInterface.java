package edu.senla.controller.controllerinterface;

public interface TypeOfContainerControllerInterface {

    public void createTypeOfContainer(String newTypeOfContainerJson);

    public String readTypeOfContainer(int id);

    public void updateTypeOfContainer(int id, String updatedTypeOfContainerJson);

    public void deleteTypeOfContainer(int id);

}
