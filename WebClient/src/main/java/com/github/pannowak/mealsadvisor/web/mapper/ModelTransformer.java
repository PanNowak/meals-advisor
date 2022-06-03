package com.github.pannowak.mealsadvisor.web.mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class ModelTransformer {

    private final ModelMapper modelMapper;

    ModelTransformer(List<Converter<?, ?>> converters) {
        this.modelMapper = createMapper(converters);
    }

    public <T> Function<Object, T> to(Class<T> destinationClass) {
        return object -> modelMapper.map(object, destinationClass);
    }

    private ModelMapper createMapper(List<Converter<?, ?>> converters) {
        var modelMapper = new ModelMapper();
        converters.forEach(modelMapper::addConverter);
        return modelMapper;
    }
}
