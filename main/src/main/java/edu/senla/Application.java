package edu.senla;

import edu.senla.controller.*;
import edu.senla.dao.OrderRepository;
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
        String client1Json = jsonWorker.getJson("jsonObjects/client1.json");
        String client2Json = jsonWorker.getJson("jsonObjects/client2.json");
        String client3Json = jsonWorker.getJson("jsonObjects/client3.json");

        clientController.createClient(client1Json);
        clientController.createClient(client2Json);
        System.out.println(clientController.readClient(clientController.getClientIdByEmail("makarova@test.com")));
        clientController.deleteClient(clientController.getClientIdByEmail("levina@test.com"));
        clientController.updateClient(clientController.getClientIdByEmail("makarova@test.com"), client3Json);
        System.out.println(clientController.readClient(clientController.getClientIdByEmail("koltsova@test.com")));

        System.out.println("For courier:");

        CourierController courierController = context.getBean(CourierController.class);
        String courier1Json = jsonWorker.getJson("jsonObjects/courier1.json");
        String courier2Json = jsonWorker.getJson("jsonObjects/courier2.json");
        String courier3Json = jsonWorker.getJson("jsonObjects/courier3.json");

        courierController.createCourier(courier1Json);
        courierController.createCourier(courier2Json);
        System.out.println(courierController.readCourier(courierController.getCourierIdByPhone("+375255555555")));
        courierController.deleteCourier(courierController.getCourierIdByPhone("+375336666666"));
        courierController.updateCourier(courierController.getCourierIdByPhone("+375255555555"), courier3Json);
        System.out.println(courierController.readCourier(courierController.getCourierIdByPhone("+375297777777")));

        System.out.println("For order:");
        OrderController orderController = context.getBean(OrderController.class);
        String order1Json = jsonWorker.getJson("jsonObjects/order1.json");
        String order2Json = jsonWorker.getJson("jsonObjects/order2.json");
        String order3Json = jsonWorker.getJson("jsonObjects/order3.json");

        int order1Id = orderController.createOrder(clientController.getClientIdByEmail("koltsova@test.com"), order1Json);
        int order2Id =orderController.createOrder(clientController.getClientIdByEmail("koltsova@test.com"), order2Json);
        System.out.println(orderController.readOrder(order1Id));
        orderController.deleteOrder(order2Id);
        System.out.println(orderController.readOrder(order1Id));

        int clientId = clientController.getClientIdByEmail("koltsova@test.com");
        System.out.println(clientController.getByIdWithOrdersJPQL(clientId));
        System.out.println(clientController.getByIdWithOrders(clientId));
        System.out.println(orderController.getAllClientsOrders(clientId));

        System.out.println("For dish:");
        DishController dishController = context.getBean(DishController.class);
        String dish1Json = jsonWorker.getJson("jsonObjects/dish1.json");
        String dish2Json = jsonWorker.getJson("jsonObjects/dish2.json");
        String dish3Json = jsonWorker.getJson("jsonObjects/dish3.json");

        dishController.createDish(dish1Json);
        dishController.createDish(dish2Json);
        System.out.println(dishController.readDish(dishController.getDishIdByName("Fried chicken")));
        dishController.deleteDish(dishController.getDishIdByName("Fried potatoes"));
        dishController.updateDish(dishController.getDishIdByName("Fried chicken"), dish3Json);
        System.out.println(dishController.readDish(dishController.getDishIdByName("Vegetable salad")));

        System.out.println("For dish information:");
        DishInformationController dishInformationController = context.getBean(DishInformationController.class);
        String dishInformation1Json = jsonWorker.getJson("jsonObjects/dishInformation1.json");
        String dishInformation2Json = jsonWorker.getJson("jsonObjects/dishInformation2.json");;
        String dishInformation3Json = jsonWorker.getJson("jsonObjects/dishInformation3.json");;

        /*int dishIdToSetInformation = dishController.getDishIdByName("Vegetable salad");
        System.out.println(dishIdToSetInformation);
        dishInformationController.createDishInformation(dishInformation1Json);
        dishInformationController.createDishInformation(dishInformation2Json);
        System.out.println(dishInformationController.readDishInformation(dishInformation1DTO.getId()));
        dishInformationController.deleteDishInformation(dishInformation2DTO.getId());
        dishInformationController.updateDishInformation(dishInformation1DTO.getId(), dishInformation3Json);
        System.out.println(dishInformationController.readDishInformation(dishInformation1DTO.getId()));*/

        /*System.out.println("For type of container:");
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


