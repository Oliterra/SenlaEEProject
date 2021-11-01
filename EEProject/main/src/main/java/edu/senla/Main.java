package edu.senla;

import edu.senla.controller.Controller;
import edu.senla.controller.ControllerInterface;
import edu.senla.di.ApplicationInit;
import edu.senla.di.Context;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String... args) {
        Context context = ApplicationInit.run("edu.senla", new HashMap<>(Map.of(ControllerInterface.class, Controller.class)));
        ControllerInterface controller = context.getObject(ControllerInterface.class);
        System.out.println(controller.doAction());
    }
}
