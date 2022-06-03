package com.github.pannowak.mealsadvisor.web;

import com.github.pannowak.springconfigurationprovider.SpringApplicationProvider;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MealsAdvisorWebServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MealsAdvisorWebServerApplication.class)
                .sources(SpringApplicationProvider.getSources())
                .run(args);
    }
}
