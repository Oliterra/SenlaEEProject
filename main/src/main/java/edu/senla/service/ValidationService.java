package edu.senla.service;

import edu.senla.service.serviceinterface.ValidationServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class ValidationService implements ValidationServiceInterface {

    public boolean isNameCorrect(String someName){
        return someName.matches("[\\p{L}| ]+");
    }

    public boolean isNameLengthValid(String someName){
        return someName.length() > 2;
    }

    public boolean isTypeOContainerNameCorrect(String typeOfContainerName) {
        for(int i = 0; i < typeOfContainerName.length(); i++){
            char x = typeOfContainerName.charAt(i);
            if(!(x == 'S' || x == 'M' || x == 'L'|| x== 'X')) return false;
        }
        return true;
    }

    public boolean isEmailCorrect(String email) {
        return email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
    }

    public boolean isPhoneCorrect(String phone) {
        return phone.length() == 13 && (phone.startsWith("+37533") || phone.startsWith("+37529") || phone.startsWith("+37544"));
    }

}
