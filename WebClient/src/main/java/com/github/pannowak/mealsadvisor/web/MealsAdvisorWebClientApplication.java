package com.github.pannowak.mealsadvisor.web;

import com.github.pannowak.springconfigurationprovider.SpringApplicationProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@PropertySource("classpath:web-client-application.properties")
public class MealsAdvisorWebClientApplication implements SpringApplicationProvider {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MealsAdvisorWebClientApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Bean
    public WebClient webClient(@Value("${base-url:http://localhost:8080}") String baseUrl,
                               ExchangeFilterFunction filter) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .filter(filter)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
