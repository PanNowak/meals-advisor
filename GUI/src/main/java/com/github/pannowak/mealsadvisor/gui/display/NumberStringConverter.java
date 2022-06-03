package com.github.pannowak.mealsadvisor.gui.display;

import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

public class NumberStringConverter extends StringConverter<BigDecimal> {

    private final NumberFormat numberFormat;

    public NumberStringConverter(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    @Override
    public String toString(BigDecimal number) {
        return number == null ? "" : numberFormat.format(number);
    }

    @Override
    public BigDecimal fromString(String string) {
        try {
            return StringUtils.isBlank(string)
                    ? BigDecimal.ZERO
                    : BigDecimal.valueOf(numberFormat.parse(string).doubleValue());
        } catch (ParseException e) {
            return BigDecimal.ZERO;
        }
    }
}
