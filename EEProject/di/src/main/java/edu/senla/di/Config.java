package edu.senla.di;

import org.reflections.Reflections;

public interface Config {
    <T> Class<? extends T> getImplClass(Class<T> intrfc);

    Reflections getScanner();
}
