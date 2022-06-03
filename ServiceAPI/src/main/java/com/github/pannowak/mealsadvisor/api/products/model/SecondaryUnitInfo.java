package com.github.pannowak.mealsadvisor.api.products.model;

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
public class SecondaryUnitInfo {

    private Long id;
    private Product product;
    private Unit unit;
    private BigDecimal toPrimaryUnitRatio;

    public SecondaryUnitInfo(Unit unit, BigDecimal toPrimaryUnitRatio) {
        this.unit = unit;
        this.toPrimaryUnitRatio = toPrimaryUnitRatio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SecondaryUnitInfo)) return false;
        SecondaryUnitInfo that = (SecondaryUnitInfo) o;
        return Objects.equals(getProductId(), that.getProductId()) &&
                Objects.equals(getUnitId(), that.getUnitId()) &&
                Objects.equals(getToPrimaryUnitRatio(), that.getToPrimaryUnitRatio());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductId(), getUnitId(), getToPrimaryUnitRatio());
    }

    @Override
    public String toString() {
        return "SecondaryUnitInfo{" +
                "id=" + id +
                ", product=" + product.getName() +
                ", unit=" + unit.getName() +
                ", toPrimaryUnitRatio=" + toPrimaryUnitRatio +
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
}
