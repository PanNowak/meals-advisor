package com.github.pannowak.mealsadvisor.web.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ModelTransformer {

    private final ModelMapper modelMapper = new ModelMapper();

    public <T> Function<Object, T> to(Class<T> destinationClass) {
        return object -> modelMapper.map(object, destinationClass);
    }
}
