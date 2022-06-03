package com.github.pannowak.mealsadvisor.web.filter.configuration;

import com.github.jknack.handlebars.EscapingStrategy;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

@Configuration
class TemplateConfiguration {

    private final Handlebars handlebars;

    TemplateConfiguration() {
        this.handlebars = new Handlebars()
                .with(EscapingStrategy.NOOP)
                .prettyPrint(true);
    }

    @Bean
    @Scope("prototype")
    public Template template(InjectionPoint injectionPoint) {
        return Optional.ofNullable(injectionPoint.getAnnotation(HBS.class))
                .map(HBS::value)
                .map(this::compile)
                .orElseThrow(() -> new IllegalStateException(
                        "Template injection point not annotated with '@HBS'"));
    }

    private Template compile(String filename) {
        try {
            return handlebars.compile("templates/" + filename);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
