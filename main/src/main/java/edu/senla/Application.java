package edu.senla;

import edu.senla.controller.*;
import edu.senla.dto.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);

        System.out.println("For client:");
        ClientController clientController = context.getBean(ClientController.class);
        String client1Json = "{\"id\" : \"0\", \"firstName\" : \"Sofia\", \"lastName\" : \"Makarova\", \"phone\" : \"+375296784510\", \"email\" : \"makarova@test.com\", \"address\" : \"Pushkin Street, Kolotushkin house\"}";
        String client2Json = "{\"id\" : \"1\", \"firstName\" : \"Arina\", \"lastName\" : \"Levina\", \"phone\" : \"+375334568134\", \"email\" : \"levina@test.com\", \"address\" : \"Kolotushkina Street, Pushkin house\"}";
        String client3Json = "{\"id\" : \"0\", \"firstName\" : \"Anna \", \"lastName\" : \"Koltsova\", \"phone\" : \"+375256767676\", \"email\" : \"koltsova@test.com\", \"address\" : \"Kolotushkina Street, Pushkin house\"}";

        ClientDTO client1DTO = new ClientDTO(0, "Sofia", "Makarova", "+375296784510", "makarova@test.com", "Pushkin Street, Kolotushkin house");
        ClientDTO client2DTO = new ClientDTO(1, "Arina", "Levina", "+375334568134", "levina@test.com", "Kolotushkina Street, Pushkin house");

        clientController.createClient(client1Json);
        clientController.createClient(client2Json);
        System.out.println(clientController.readClient(client1DTO.getId()));
        clientController.deleteClient(client2DTO.getId());
        clientController.updateClient(client1Json, client3Json);
        System.out.println(clientController.readClient(client1DTO.getId()));

        System.out.println("For courier:");
        CourierController courierController = context.getBean(CourierController.class);
        String courier1Json = "{\"id\" : \"0\", \"firstName\" : \"Matvey\", \"lastName\" : \"Zaitsev\"}";
        String courier2Json = "{\"id\" : \"1\", \"firstName\" : \"George\", \"lastName\" : \"Drozdov\"}";
        String courier3Json = "{\"id\" : \"0\", \"firstName\" : \"Konstantin\", \"lastName\" : \"Sergeyev\"}";

        CourierDTO courier1DTO = new CourierDTO(0, "Matvey", "Zaitsev");
        CourierDTO courier2DTO = new CourierDTO(1, "George", "Drozdov");

        courierController.createCourier(courier1Json);
        courierController.createCourier(courier2Json);
        System.out.println(courierController.readCourier(courier1DTO.getId()));
        courierController.deleteCourier(courier2DTO.getId());
        courierController.updateCourier(courier1Json, courier3Json);
        System.out.println(courierController.readCourier(courier1DTO.getId()));

        System.out.println("For dish:");
        DishController dishController = context.getBean(DishController.class);
        String dish1Json = "{\"id\" : \"0\", \"name\" : \"Fried chicken\"}";
        String dish2Json = "{\"id\" : \"1\", \"name\" : \"Barbecue\"}";
        String dish3Json = "{\"id\" : \"0\", \"name\" : \"Baked pork\"}";

        DishDTO dish1DTO = new DishDTO(0, "Fried chicken");
        DishDTO dish2DTO = new DishDTO(1, "Barbecue");

        dishController.createDish(dish1Json);
        dishController.createDish(dish2Json);
        System.out.println(dishController.readDish(dish1DTO.getId()));
        dishController.deleteDish(dish2DTO.getId());
        dishController.updateDish(dish1Json, dish3Json);
        System.out.println(dishController.readDish(dish1DTO.getId()));

        System.out.println("For dish information:");
        DishInformationController dishInformationController = context.getBean(DishInformationController.class);
        String dishInformation1Json = "{\"id\" : \"0\", \"description\" : \"Acceptable\", \"caloricContent\" : \"278\"}";
        String dishInformation2Json = "{\"id\" : \"1\", \"description\" : \"Delicious\", \"caloricContent\" : \"425\"}";
        String dishInformation3Json = "{\"id\" : \"0\", \"description\" : \"Very tasty\", \"caloricContent\" : \"389\"}";

        DishInformationDTO dishInformation1DTO = new DishInformationDTO(0, "Acceptable", 278);
        DishInformationDTO dishInformation2DTO = new DishInformationDTO(1, "Delicious", 425);

        dishInformationController.createDishInformation(dishInformation1Json);
        dishInformationController.createDishInformation(dishInformation2Json);
        System.out.println(dishInformationController.readDishInformation(dishInformation1DTO.getId()));
        dishInformationController.deleteDishInformation(dishInformation2DTO.getId());
        dishInformationController.updateDishInformation(dishInformation1Json, dishInformation3Json);
        System.out.println(dishInformationController.readDishInformation(dishInformation1DTO.getId()));

        System.out.println("For order:");
        OrderController orderController = context.getBean(OrderController.class);
        String order1Json = "{\"id\" : \"0\", \"paymentType\" : \"by card\", \"status\" : \"new\"}";
        String order2Json = "{\"id\" : \"1\", \"paymentType\" : \"in cash\", \"status\" : \"new\"}";
        String order3Json = "{\"id\" : \"0\", \"paymentType\" : \"in cash\", \"status\" : \"in process\"}";

        OrderDTO order1DTO = new OrderDTO(0, "by card", "new");
        OrderDTO order2DTO = new OrderDTO(1, "in cash", "new");

        orderController.createOrder(order1Json);
        orderController.createOrder(order2Json);
        System.out.println(orderController.readOrder(order1DTO.getId()));
        orderController.deleteOrder(order2DTO.getId());
        orderController.updateOrder(order1Json, order3Json);
        System.out.println(orderController.readOrder(order1DTO.getId()));

        System.out.println("For type of container:");
        TypeOfContainerController typeOfContainerController = context.getBean(TypeOfContainerController.class);
        String typeOfContainer1Json = "{\"numberOfCalories\" : \"0\", \"name\" : \"small\", \"price\" : \"5\"}";
        String typeOfContainer2Json = "{\"numberOfCalories\" : \"1\", \"name\" : \"medium\", \"price\" : \"10\"}";
        String typeOfContainer3Json = "{\"numberOfCalories\" : \"0\", \"name\" : \"large\", \"price\" : \"15\"}";

        TypeOfContainerDTO typeOfContainer1DTO = new TypeOfContainerDTO(0, "small", 5);
        TypeOfContainerDTO typeOfContainer2DTO = new TypeOfContainerDTO(1, "medium", 10);

        typeOfContainerController.createTypeOfContainer(typeOfContainer1Json);
        typeOfContainerController.createTypeOfContainer(typeOfContainer2Json);
        System.out.println(typeOfContainerController.readTypeOfContainer(typeOfContainer1DTO.getNumberOfCalories()));
        typeOfContainerController.deleteTypeOfContainer(typeOfContainer2DTO.getNumberOfCalories());
        typeOfContainerController.updateTypeOfContainer(typeOfContainer1Json, typeOfContainer3Json);
        System.out.println(typeOfContainerController.readTypeOfContainer(typeOfContainer1DTO.getNumberOfCalories()));

    }
}
