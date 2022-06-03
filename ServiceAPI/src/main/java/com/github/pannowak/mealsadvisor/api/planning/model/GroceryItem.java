package com.github.pannowak.mealsadvisor.api.planning.model;

import com.github.pannowak.mealsadvisor.api.products.model.ProductSummary;
import com.github.pannowak.mealsadvisor.api.units.model.Unit;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class GroceryItem {

    ProductSummary productSummary;
    BigDecimal numberOfUnits;
    Unit unit;
}
