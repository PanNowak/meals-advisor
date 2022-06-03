package com.github.pannowak.mealsadvisor.gui.controls;

import com.github.pannowak.mealsadvisor.gui.display.NumberStringConverter;
import com.github.pannowak.mealsadvisor.gui.display.RegexFilter;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class TextFormatterFactory {

    public static TextFormatter<BigDecimal> create(Locale locale) {
        StringConverter<BigDecimal> valueConverter = getConverter(locale);
        UnaryOperator<Change> filter = getFilter(locale);
        return new TextFormatter<>(valueConverter, BigDecimal.ONE, filter);
    }

    private static StringConverter<BigDecimal> getConverter(Locale locale) {
        var numberFormat = NumberFormat.getNumberInstance(locale);
        return new NumberStringConverter(numberFormat);
    }

    private static UnaryOperator<Change> getFilter(Locale locale) {
        char decimalSeparator = DecimalFormatSymbols.getInstance(locale).getDecimalSeparator();
        String numberPattern = "([0-9]?|([1-9][0-9]*))(" + decimalSeparator + "[0-9]{0,3})?";
        return new RegexFilter(Pattern.compile(numberPattern));
    }
}
