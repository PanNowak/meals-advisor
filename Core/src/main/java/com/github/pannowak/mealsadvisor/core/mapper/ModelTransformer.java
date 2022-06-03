package com.github.pannowak.mealsadvisor.core.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("coreModelTransformer")
public class ModelTransformer {

    private final ModelMapper modelMapper = new ModelMapper();

    public <T> Function<Object, T> to(Class<T> destinationClass) {
        return object -> modelMapper.map(object, destinationClass);
    }
}
