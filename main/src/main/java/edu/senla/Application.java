package edu.senla;

import edu.senla.controller.*;
import edu.senla.dto.*;
import edu.senla.helper.JsonWorker;
import lombok.SneakyThrows;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class Application {

    @SneakyThrows
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);

        JsonWorker jsonWorker = context.getBean(JsonWorker.class);

        System.out.println("For client:");
        ClientController clientController = context.getBean(ClientController.class);
        String client1Json = jsonWorker.getJson("client1.json");
        String client2Json = jsonWorker.getJson("client2.json");
        String client3Json = jsonWorker.getJson("client3.json");

        ClientDTO client1DTO = new ClientDTO(0, "Sofia", "Makarova", "+375296784510", "makarova@test.com", "Pushkin Street, Kolotushkin house");
        ClientDTO client2DTO = new ClientDTO(1, "Arina", "Levina", "+375334568134", "levina@test.com", "Kolotushkina Street, Pushkin house");

        clientController.createClient(client1Json);
        clientController.createClient(client2Json);
        System.out.println(clientController.readClient(client1DTO.getId()));
        clientController.deleteClient(client2DTO.getId());
        clientController.updateClient(client1DTO.getId(), client3Json);
        System.out.println(clientController.readClient(client1DTO.getId()));

        /*System.out.println("For courier:");
        CourierController courierController = context.getBean(CourierController.class);
        String courier1Json = jsonWorker.getJson("courier1.json");
        String courier2Json = jsonWorker.getJson("courier2.json");
        String courier3Json = jsonWorker.getJson("courier3.json");

        CourierDTO courier1DTO = new CourierDTO(0, "Matvey", "Zaitsev");
        CourierDTO courier2DTO = new CourierDTO(1, "George", "Drozdov");

        courierController.createCourier(courier1Json);
        courierController.createCourier(courier2Json);
        System.out.println(courierController.readCourier(courier1DTO.getId()));
        courierController.deleteCourier(courier2DTO.getId());
        courierController.updateCourier(courier1DTO.getId(), courier3Json);
        System.out.println(courierController.readCourier(courier1DTO.getId()));

        System.out.println("For dish:");
        DishController dishController = context.getBean(DishController.class);
        String dish1Json = jsonWorker.getJson("dish1.json");
        String dish2Json = jsonWorker.getJson("dish2.json");
        String dish3Json = jsonWorker.getJson("dish3.json");

        DishDTO dish1DTO = new DishDTO(0, "Fried chicken");
        DishDTO dish2DTO = new DishDTO(1, "Barbecue");

        dishController.createDish(dish1Json);
        dishController.createDish(dish2Json);
        System.out.println(dishController.readDish(dish1DTO.getId()));
        dishController.deleteDish(dish2DTO.getId());
        dishController.updateDish(dish1DTO.getId(), dish3Json);
        System.out.println(dishController.readDish(dish1DTO.getId()));

        System.out.println("For dish information:");
        DishInformationController dishInformationController = context.getBean(DishInformationController.class);
        String dishInformation1Json = jsonWorker.getJson("dishInformation1.json");
        String dishInformation2Json = jsonWorker.getJson("dishInformation2.json");;
        String dishInformation3Json = jsonWorker.getJson("dishInformation3.json");;

        DishInformationDTO dishInformation1DTO = new DishInformationDTO(0, "Acceptable", 278);
        DishInformationDTO dishInformation2DTO = new DishInformationDTO(1, "Delicious", 425);

        dishInformationController.createDishInformation(dishInformation1Json);
        dishInformationController.createDishInformation(dishInformation2Json);
        System.out.println(dishInformationController.readDishInformation(dishInformation1DTO.getId()));
        dishInformationController.deleteDishInformation(dishInformation2DTO.getId());
        dishInformationController.updateDishInformation(dishInformation1DTO.getId(), dishInformation3Json);
        System.out.println(dishInformationController.readDishInformation(dishInformation1DTO.getId()));

        System.out.println("For order:");
        OrderController orderController = context.getBean(OrderController.class);
        String order1Json = jsonWorker.getJson("order1.json");
        String order2Json = jsonWorker.getJson("order2.json");
        String order3Json = jsonWorker.getJson("order3.json");

        OrderDTO order1DTO = new OrderDTO(0, "by card", "new");
        OrderDTO order2DTO = new OrderDTO(1, "in cash", "new");

        orderController.createOrder(order1Json);
        orderController.createOrder(order2Json);
        System.out.println(orderController.readOrder(order1DTO.getId()));
        orderController.deleteOrder(order2DTO.getId());
        orderController.updateOrder(order1DTO.getId(), order3Json);
        System.out.println(orderController.readOrder(order1DTO.getId()));

        System.out.println("For type of container:");
        TypeOfContainerController typeOfContainerController = context.getBean(TypeOfContainerController.class);
        String typeOfContainer1Json = jsonWorker.getJson("typeOfContainer1.json");
        String typeOfContainer2Json = jsonWorker.getJson("typeOfContainer2.json");
        String typeOfContainer3Json = jsonWorker.getJson("typeOfContainer3.json");

        TypeOfContainerDTO typeOfContainer1DTO = new TypeOfContainerDTO(0, "small", 5);
        TypeOfContainerDTO typeOfContainer2DTO = new TypeOfContainerDTO(1, "medium", 10);

        typeOfContainerController.createTypeOfContainer(typeOfContainer1Json);
        typeOfContainerController.createTypeOfContainer(typeOfContainer2Json);
        System.out.println(typeOfContainerController.readTypeOfContainer(typeOfContainer1DTO.getNumberOfCalories()));
        typeOfContainerController.deleteTypeOfContainer(typeOfContainer2DTO.getNumberOfCalories());
        typeOfContainerController.updateTypeOfContainer(typeOfContainer1DTO.getNumberOfCalories(), typeOfContainer3Json);
        System.out.println(typeOfContainerController.readTypeOfContainer(typeOfContainer1DTO.getNumberOfCalories()));*/

        context.registerShutdownHook();
    }
}


