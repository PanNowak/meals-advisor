package com.github.pannowak.mealsadvisor.core.planning.service;

import com.github.pannowak.mealsadvisor.api.meals.model.Ingredient;
import com.github.pannowak.mealsadvisor.api.products.model.Product;
import com.github.pannowak.mealsadvisor.api.products.model.SecondaryUnitInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

final class NumberOfPrimaryUnitsCalculator {

    public static BigDecimal calculate(Ingredient ingredient) {
        return new NumberOfPrimaryUnitsCalculator(ingredient).calculate();
    }

    private final Ingredient ingredient;

    NumberOfPrimaryUnitsCalculator(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public BigDecimal calculate() {
        Product product = ingredient.getProduct();
        if (product.getPrimaryUnit().equals(ingredient.getUnit())) {
            return ingredient.getNumberOfUnits();
        } else {
            return calculateNumberOfPrimaryUnits(product.getSecondaryUnits());
        }
    }

    private BigDecimal calculateNumberOfPrimaryUnits(Collection<SecondaryUnitInfo> secondaryUnits) {
        for (SecondaryUnitInfo secondaryUnitInfo : secondaryUnits) {
            if (secondaryUnitInfo.getUnit().equals(ingredient.getUnit())) {
                return ingredient.getNumberOfUnits().divide(
                        secondaryUnitInfo.getToPrimaryUnitRatio(), RoundingMode.HALF_EVEN);
            }
        }

        throw new RuntimeException("Impossible!");
        //TODO na pewno trzeba poprawić + uniemożliwić usuwanie jednostek z produktów
    }
}
