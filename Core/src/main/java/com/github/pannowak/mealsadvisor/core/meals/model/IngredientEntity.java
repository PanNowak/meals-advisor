package com.github.pannowak.mealsadvisor.core.meals.model;

import com.github.pannowak.mealsadvisor.core.products.model.ProductEntity;
import com.github.pannowak.mealsadvisor.core.units.model.UnitEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "INGREDIENTS")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
public class IngredientEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Positive
    @Column(precision = 20, scale = 3)
    private BigDecimal numberOfUnits;

    @NotNull
    @ManyToOne(optional = false)
    private ProductEntity product;

    @NotNull
    @ManyToOne(optional = false)
    private UnitEntity unit;

    @NotNull
    @ManyToOne(optional = false)
    private MealEntity meal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IngredientEntity)) return false;
        IngredientEntity that = (IngredientEntity) o;
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
                .map(ProductEntity::getId)
                .orElse(null);
    }

    private Long getUnitId() {
        return Optional.ofNullable(getUnit())
                .map(UnitEntity::getId)
                .orElse(null);
    }

    private Long getMealId() {
        return Optional.ofNullable(getMeal())
                .map(MealEntity::getId)
                .orElse(null);
    }
}
