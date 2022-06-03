package com.github.pannowak.springconfigurationprovider;

import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

public interface SpringApplicationProvider {

    static Class<?>[] getSources() {
        return ServiceLoader.load(SpringApplicationProvider.class).stream()
                .map(Provider::type)
                .toArray(Class[]::new);
    }
}
