package edu.senla.di;

import java.util.Map;

public class ApplicationInit {
    public static Context run(String packageToScan, Map<Class, Class> intrfcToImplClass){
        JavaConfig config = new JavaConfig(packageToScan, intrfcToImplClass);
        Context context = new Context(config);
        ObjectFactory objectFactory = new ObjectFactory((context));
        context.setFactory(objectFactory);
        return context;
    }
}
