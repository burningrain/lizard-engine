package com.github.burningrain.lizard.engine.core;

import java.util.HashMap;

/**
 * @author burningrain on 22.05.2018.
 */
public class InternalContext {

    private HashMap<Class, Object> singletons = new HashMap<>();
    private HashMap<Class, Class> prototypes = new HashMap<>();

    public void addSingleton(Class interfaceClazz, Object object) {
        if(object == null) throw new IllegalArgumentException("Добавление пустого объекта недопустимо");
        singletons.put(interfaceClazz, object);
    }

    public <S> S getSingleton(Class<S> clazz) {
        return clazz.cast(singletons.get(clazz));
    }

    public <P> void addPrototype(Class<P> interfaceClazz, Class prototype) {
        if(prototype == null) throw new IllegalArgumentException("Добавление пустого объекта недопустимо");
        prototypes.put(interfaceClazz, prototype);
    }

    public <P> P getPrototype(Class<P> interfaceClazz) {
        Class<P> clazz = prototypes.get(interfaceClazz);
        return interfaceClazz.cast(createObject(clazz));
    }

    private static <T> T createObject(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
