package com.github.pannowak.mealsadvisor.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

@Configuration
class SwaggerConfig {

    @Bean
    public Docket swagger() {
        return new Docket(DocumentationType.OAS_30)
                .globalRequestParameters(globalRequestParameters())
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo());
    }

    private List<RequestParameter> globalRequestParameters() {
        return List.of(
                new RequestParameterBuilder()
                        .name(HttpHeaders.ACCEPT_LANGUAGE)
                        .in(ParameterType.HEADER)
                        .parameterIndex(Integer.MAX_VALUE)
                        .build()
        );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Meals Advisor")
                .version("1.0")
                .description("REST API for managing Meals Advisor Web App")
                .contact(new Contact("Mateusz Nowak", "", "nowak.mateusz@outlook.com"))
                .license("Apache License Version 2.0")
                .build();
    }
}
