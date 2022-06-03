package com.github.pannowak.mealsadvisor.web.config;

import com.github.pannowak.mealsadvisor.web.controller.MealController;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.reactive.config.PathMatchConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
class WebConfig implements WebFluxConfigurer {

    private final ApiPathConfigurer apiPathConfigurer;

    WebConfig(ApiPathConfigurer apiPathConfigurer) {
        this.apiPathConfigurer = apiPathConfigurer;
    }

    @Override
    public void configurePathMatching(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(apiPathConfigurer.getApiPathPrefix(),
                HandlerTypePredicate.forAnnotation(RestController.class)
                        .and(HandlerTypePredicate.forBasePackageClass(MealController.class)));
    }
}
