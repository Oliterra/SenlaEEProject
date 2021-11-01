package edu.senla.di;

import lombok.Getter;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;

public class JavaConfig implements Config{
    @Getter
    private Reflections scanner;
    private Map<Class, Class> intrfcToImplClass;

    public JavaConfig(String packageToScan, Map<Class, Class> intrfcToImplClass) {
        this.intrfcToImplClass = intrfcToImplClass;
        this.scanner = new Reflections(packageToScan);
    }

    @Override
    public <T> Class<? extends T> getImplClass(Class<T> intrfc) {
        return intrfcToImplClass.computeIfAbsent(intrfc, c -> {
            Set<Class<? extends T>> classes = scanner.getSubTypesOf(intrfc);
            if (classes.size() != 1) {
                throw new RuntimeException(intrfc + " has 0 or >1 implementations, please update your config");
            }
            return classes.iterator().next();
        });
    }
}
