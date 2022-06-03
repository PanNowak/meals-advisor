package com.github.pannowak.mealsadvisor.api.internationalization;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import static java.util.ResourceBundle.*;

public final class MessageDictionary {

    private final ResourceBundle defaultBundle;
    private final ResourceBundle localizedBundle;

    public MessageDictionary(String resourceBundleBaseName, Locale locale) {
        Control noFallbackControl = Control.getNoFallbackControl(Control.FORMAT_DEFAULT);
        this.defaultBundle = getBundle(resourceBundleBaseName, Locale.US, noFallbackControl);
        this.localizedBundle = getBundle(resourceBundleBaseName, locale, noFallbackControl);
    }

    public String getMessage(String messageKey, Object... args) {
        var messageTemplate = defaultBundle.getString(messageKey);
        return MessageFormat.format(messageTemplate, args);
    }

    public String getLocalizedMessage(String messageKey, Object... args) {
        var messageTemplate = localizedBundle.getString(messageKey);
        return MessageFormat.format(messageTemplate, args);
    }
}
