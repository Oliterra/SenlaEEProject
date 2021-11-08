package edu.senla;

import edu.senla.controller.ClientController;
import edu.senla.dao.ClientDAO;
import edu.senla.dto.ClientDTO;
import edu.senla.entity.Client;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        ClientController clientController = context.getBean(ClientController.class);

        String client1Json = "{\"id\" : \"0\", \"firstName\" : \"Sofia\", \"lastName\" : \"Makarova\", \"phone\" : \"+375296784510\", \"email\" : \"makarova@test.com\", \"address\" : \"Pushkin Street, Kolotushkin house\"}";
        String client2Json = "{\"id\" : \"1\", \"firstName\" : \"Arina\", \"lastName\" : \"Levina\", \"phone\" : \"+375334568134\", \"email\" : \"levina@test.com\", \"address\" : \"Kolotushkina Street, Pushkin house\"}";
        String client3 = "{\"id\" : \"0\", \"firstName\" : \"Anna \", \"lastName\" : \"Koltsova\", \"phone\" : \"+375256767676\", \"email\" : \"koltsova@test.com\", \"address\" : \"Kolotushkina Street, Pushkin house\"}";

        ClientDTO client1DTO = new ClientDTO(0, "Sofia", "Makarova", "+375296784510", "makarova@test.com", "Pushkin Street, Kolotushkin house");
        ClientDTO client2DTO = new ClientDTO(1, "Arina", "Levina", "+375334568134", "levina@test.com", "Kolotushkina Street, Pushkin house");

        clientController.createClient(client1Json);
        clientController.createClient(client2Json);
        System.out.println(clientController.read(client1DTO));
        clientController.delete(client2DTO);
        clientController.update(client1DTO, client3);
        System.out.println(clientController.read(client1DTO));

    }
}
