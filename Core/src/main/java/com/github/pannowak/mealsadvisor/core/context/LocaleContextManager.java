package com.github.pannowak.mealsadvisor.core.context;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Component
public class LocaleContextManager {

    @PostConstruct
    void initialize() {
        LocaleContextHolder.setDefaultLocale(Locale.ENGLISH);
    }

    public Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }

    void setCurrentLocale(Locale locale) {
        LocaleContextHolder.setLocale(locale);
    }
}
