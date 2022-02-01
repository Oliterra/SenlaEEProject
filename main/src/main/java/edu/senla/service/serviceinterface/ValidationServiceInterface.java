package edu.senla.service.serviceinterface;

public interface ValidationServiceInterface {

    public boolean isNameCorrect(String someName);

    public boolean isNameLengthValid(String someName);

    public boolean isTypeOContainerNameCorrect(String typeOfContainerName);

    public boolean isEmailCorrect(String email);

    public boolean isPhoneCorrect(String phone);

}
