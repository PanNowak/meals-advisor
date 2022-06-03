package com.github.pannowak.mealsadvisor.api;

@FunctionalInterface
public interface Summarizable<T extends Summary> {

    T toSummary();
}
