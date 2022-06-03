package com.github.pannowak.mealsadvisor.api.products.model;

import com.github.pannowak.mealsadvisor.api.Summarizable;
import com.github.pannowak.mealsadvisor.api.units.model.Unit;
import lombok.*;

import java.util.*;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
public class Product implements Summarizable<ProductSummary> {

    private Long id;

    @EqualsAndHashCode.Include
    private String name;
    private Unit primaryUnit;
    private Collection<SecondaryUnitInfo> secondaryUnits = new ArrayList<>();

    public void addSecondaryUnit(SecondaryUnitInfo unitInfo) {
        secondaryUnits.add(unitInfo);
        unitInfo.setProduct(this);
    }

    public void removeSecondaryUnit(SecondaryUnitInfo unitInfo) {
        secondaryUnits.remove(unitInfo);
        unitInfo.setProduct(null);
    }

    public Set<Unit> getAllSupportedUnits() {
        Set<Unit> units = new HashSet<>();
        units.add(primaryUnit);
        secondaryUnits.forEach(s -> units.add(s.getUnit()));
        return Collections.unmodifiableSet(units);
    }

    public ProductSummary toSummary() {
        return new ProductSummary(id, name);
    }
}
