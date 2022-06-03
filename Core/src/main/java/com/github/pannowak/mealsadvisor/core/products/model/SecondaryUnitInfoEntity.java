package com.github.pannowak.mealsadvisor.core.products.model;

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
@Table(name = "PRODUCT_TO_UNIT")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
public class SecondaryUnitInfoEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    private ProductEntity product;

    @NotNull
    @ManyToOne(optional = false)
    private UnitEntity unit;

    @NotNull
    @Positive
    @Column(precision = 20, scale = 3)
    private BigDecimal toPrimaryUnitRatio;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SecondaryUnitInfoEntity)) return false;
        SecondaryUnitInfoEntity that = (SecondaryUnitInfoEntity) o;
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
                .map(ProductEntity::getId)
                .orElse(null);
    }

    private Long getUnitId() {
        return Optional.ofNullable(getUnit())
                .map(UnitEntity::getId)
                .orElse(null);
    }
}
