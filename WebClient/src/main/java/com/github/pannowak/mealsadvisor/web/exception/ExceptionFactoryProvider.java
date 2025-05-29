package com.github.pannowak.mealsadvisor.web.exception;

import com.github.pannowak.mealsadvisor.api.internationalization.MessageDictionary;
import org.springframework.stereotype.Component;
import reactor.util.context.ContextView; // Changed from Context

import java.util.Locale;

@Component
public class ExceptionFactoryProvider {

    private static final String EXCEPTION_BUNDLE_NAME = ExceptionFactory.class.getPackageName() + ".ExceptionMessages";

    public ExceptionFactory get(ContextView contextView) { // Changed from Context to ContextView
        var locale = contextView.<Locale>getOrEmpty(Locale.class).orElse(Locale.US); // Changed here
        var messageDictionary = new MessageDictionary(EXCEPTION_BUNDLE_NAME, locale);
        return new ExceptionFactory(messageDictionary);
    }
}
