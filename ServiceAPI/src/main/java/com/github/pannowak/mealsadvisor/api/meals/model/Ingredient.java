package com.github.pannowak.mealsadvisor.api.meals.model;

import com.github.pannowak.mealsadvisor.api.products.model.Product;
import com.github.pannowak.mealsadvisor.api.units.model.Unit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class Ingredient {

    private Long id;
    private BigDecimal numberOfUnits;
    private Product product;
    private Unit unit;
    private Meal meal;

    public Ingredient(Product product, BigDecimal numberOfUnits, Unit unit) {
        this.product = product;
        this.numberOfUnits = numberOfUnits;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient)) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(getNumberOfUnits(), that.getNumberOfUnits()) &&
                Objects.equals(getProductId(), that.getProductId()) &&
                Objects.equals(getUnitId(), that.getUnitId()) &&
                Objects.equals(getMealId(), that.getMealId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumberOfUnits(), getProductId(), getUnitId(), getMealId());
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", numberOfUnits=" + numberOfUnits +
                ", product=" + product.getName() +
                ", unit=" + unit.getName() +
                ", meal=" + meal.getName() +
                '}';
    }

    private Long getProductId() {
        return Optional.ofNullable(getProduct())
                .map(Product::getId)
                .orElse(null);
    }

    private Long getUnitId() {
        return Optional.ofNullable(getUnit())
                .map(Unit::getId)
                .orElse(null);
    }

    private Long getMealId() {
        return Optional.ofNullable(getMeal())
                .map(Meal::getId)
                .orElse(null);
    }
}
