package edu.senla.service;

public interface ValidationService {

    boolean isNameCorrect(String someName);

    boolean isNameLengthValid(String someName);

    boolean isTypeOContainerNameCorrect(String typeOfContainerName);

    boolean isEmailCorrect(String email);

    boolean isPhoneCorrect(String phone);
}
