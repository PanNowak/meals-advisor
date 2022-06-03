package com.github.pannowak.mealsadvisor.web.client;

import java.util.stream.Collectors;

public final class TestStringUtils {

    public static String toSingleLine(String multiLineString) {
        return multiLineString.lines()
                .map(String::strip)
                .collect(Collectors.joining());
    }

    private TestStringUtils() {}
}
