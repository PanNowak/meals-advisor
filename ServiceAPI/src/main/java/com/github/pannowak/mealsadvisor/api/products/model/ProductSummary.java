package com.github.pannowak.mealsadvisor.api.products.model;

import com.github.pannowak.mealsadvisor.api.Summary;
import lombok.Value;

@Value
public class ProductSummary implements Summary {

    Long id;
    String name;
}
