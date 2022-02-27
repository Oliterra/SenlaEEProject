package edu.senla.service.impl;

import edu.senla.config.RandomDatabaseFillingConfig;
import edu.senla.model.dto.CourierRegistrationRequestDTO;
import edu.senla.model.dto.RegistrationRequestDTO;
import edu.senla.model.dto.UserBasicInfoDTO;
import edu.senla.service.CourierService;
import edu.senla.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RandomDatabaseFillingServiceImpl extends AbstractService{

    private final RandomDatabaseFillingConfig config;
    private final UserService userService;
    private final CourierService courierService;

    @SneakyThrows
    public void createCertainNumberOfUsers(int number){
        for (int i = 0; i < number; i++) {
            RegistrationRequestDTO newUser = getRandomUser();
            String newUserString = objectMapper.writeValueAsString(newUser);
            userService.createClient(newUserString);
        }
    }

    @SneakyThrows
    public void createCertainNumberOfCouriers(int number){
        for (int i = 0; i < number; i++) {
            CourierRegistrationRequestDTO newCourier = getRandomCourier();
            String newCourierString = objectMapper.writeValueAsString(newCourier);
            courierService.createCourier(newCourierString);
        }
    }

    private RegistrationRequestDTO getRandomUser(){
        RegistrationRequestDTO registrationRequestDTO = new RegistrationRequestDTO();
        Random random = new Random();
        UserBasicInfoDTO randomUser = new UserBasicInfoDTO();
        boolean gender = random.nextBoolean();
        if (gender) randomUser = getRandomFemale();
        else randomUser = getRandomMale();
        StringBuilder sb = new StringBuilder();
        String firstName = randomUser.getFirstName();
        String lastName = randomUser.getLastName();
        registrationRequestDTO.setFirstName(firstName);
        registrationRequestDTO.setLastName(lastName);
        String email = sb.append(lastName.toLowerCase(Locale.ROOT)).append("@gmail.com").toString();
        registrationRequestDTO.setEmail(email);
        String phone = getRandomPhone();
        registrationRequestDTO.setPhone(phone);
        registrationRequestDTO.setUsername(lastName.toLowerCase(Locale.ROOT));
        registrationRequestDTO.setPassword(firstName.toLowerCase(Locale.ROOT));
        registrationRequestDTO.setPasswordConfirm(firstName.toLowerCase(Locale.ROOT));
        return registrationRequestDTO;
    }

    private CourierRegistrationRequestDTO getRandomCourier(){
        CourierRegistrationRequestDTO courierRegistrationRequestDTO = new CourierRegistrationRequestDTO();
        Random random = new Random();
        UserBasicInfoDTO randomUser = new UserBasicInfoDTO();
        boolean gender = random.nextBoolean();
        if (gender) randomUser = getRandomFemale();
        else randomUser = getRandomMale();
        StringBuilder sb = new StringBuilder();
        String firstName = randomUser.getFirstName();
        String lastName = randomUser.getLastName();
        courierRegistrationRequestDTO.setFirstName(firstName);
        courierRegistrationRequestDTO.setLastName(lastName);
        String phone = getRandomPhone();
        courierRegistrationRequestDTO.setPhone(phone);
        courierRegistrationRequestDTO.setPassword(firstName.toLowerCase(Locale.ROOT));
        courierRegistrationRequestDTO.setPasswordConfirm(firstName.toLowerCase(Locale.ROOT));
        return courierRegistrationRequestDTO;
    }

    private String getRandomPhone() {
        int intPhone = (int) (Math.random() * (9999999 - 1000000) + 1000000);
        return  "+37533" + String.valueOf(intPhone);
    }

    private UserBasicInfoDTO getRandomFemale(){
        UserBasicInfoDTO randomFemale = new UserBasicInfoDTO();
        List<String> femaleFirstNames = config.getFemaleFirstNames();
        List<String> femaleLastNames = config.getFemaleLastNames();
        int randomFemaleFirstNameIndex = (int) (Math.random() * femaleFirstNames.size());
        int randomFemaleLastNameIndex = (int) (Math.random() * femaleLastNames.size());
        String randomFemaleFirstName = femaleFirstNames.get(randomFemaleFirstNameIndex);
        String randomFemaleLastName = femaleLastNames.get(randomFemaleLastNameIndex);
        randomFemale.setFirstName(randomFemaleFirstName);
        randomFemale.setLastName(randomFemaleLastName);
        return randomFemale;
    }

    private UserBasicInfoDTO getRandomMale(){
        UserBasicInfoDTO randomMale = new UserBasicInfoDTO();
        List<String> maleFirstNames = config.getMaleFirstNames();
        List<String> maleLastNames = config.getMaleLastNames();
        int randomMaleFirstNameIndex = (int) (Math.random() * maleFirstNames.size());
        int randomMaleLastNameIndex = (int) (Math.random() * maleLastNames.size());
        String randomMaleFirstName = maleFirstNames.get(randomMaleFirstNameIndex);
        String randomMaleLastName = maleLastNames.get(randomMaleLastNameIndex);
        randomMale.setFirstName(randomMaleFirstName);
        randomMale.setLastName(randomMaleLastName);
        return randomMale;
    }
}
