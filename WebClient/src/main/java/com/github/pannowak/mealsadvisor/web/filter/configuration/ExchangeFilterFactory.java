package com.github.pannowak.mealsadvisor.web.filter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.Collections;
import java.util.List;

@Configuration
class ExchangeFilterFactory {

    @Bean
    @Primary
    public ExchangeFilterFunction exchangeFilterFunction(List<ExchangeFilterFunction> filters) {
        Collections.reverse(filters);
        return filters.stream()
                .reduce(ExchangeFilterFunction::andThen)
                .orElseGet(this::noOpFilter);
    }

    private ExchangeFilterFunction noOpFilter() {
        return (request, next) -> next.exchange(request);
    }
}
