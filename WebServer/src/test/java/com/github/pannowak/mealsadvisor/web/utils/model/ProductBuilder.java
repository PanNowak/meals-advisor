package com.github.pannowak.mealsadvisor.web.utils.model;

import com.github.pannowak.mealsadvisor.api.products.model.Product;
import com.github.pannowak.mealsadvisor.api.products.model.SecondaryUnitInfo;
import com.github.pannowak.mealsadvisor.api.units.model.Unit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class ProductBuilder {

    public static PrimaryUnitInfoBuilder createProduct(Long productId, String productName) {
        return new InnerBuilder(productId, productName);
    }

    public static PrimaryUnitInfoBuilder createProduct(String productName) {
        return new InnerBuilder(null, productName);
    }

    private static final class InnerBuilder implements PrimaryUnitInfoBuilder, SecondaryUnitInfoBuilder {

        private final Product product;
        private final List<SecondaryUnitInfo> secondaryUnits;

        private InnerBuilder(Long productId, String productName) {
            this.product = createProduct(productId, productName);
            this.secondaryUnits = new ArrayList<>();
        }

        @Override
        public SecondaryUnitInfoBuilder withPrimaryUnit(Long unitId, String unitName) {
            var unit = createUnit(unitId, unitName);
            product.setPrimaryUnit(unit);
            return this;
        }

        @Override
        public SecondaryUnitInfoBuilder withSecondaryUnit(Long id, String ratio, String unitName) {
            var unit = createUnit(id, unitName);
            var secondaryUnit = createSecondaryUnit(id, ratio, unit);
            secondaryUnits.add(secondaryUnit);
            return this;
        }

        @Override
        public Product build() {
            product.setSecondaryUnits(secondaryUnits);
            return product;
        }

        private Product createProduct(Long productId, String productName) {
            var product = new Product();
            product.setId(productId);
            product.setName(productName);
            return product;
        }

        private Unit createUnit(Long unitId, String unitName) {
            var unit = new Unit();
            unit.setId(unitId);
            unit.setName(unitName);
            return unit;
        }

        private SecondaryUnitInfo createSecondaryUnit(Long id, String ratio, Unit unit) {
            var secondaryUnit = new SecondaryUnitInfo();
            secondaryUnit.setId(id);
            secondaryUnit.setToPrimaryUnitRatio(new BigDecimal(ratio));
            secondaryUnit.setUnit(unit);
            secondaryUnit.setProduct(product);
            return secondaryUnit;
        }
    }

    public interface PrimaryUnitInfoBuilder {

        SecondaryUnitInfoBuilder withPrimaryUnit(Long unitId, String unitName);

        Product build();
    }

    public interface SecondaryUnitInfoBuilder {

        SecondaryUnitInfoBuilder withSecondaryUnit(Long id, String ratio, String unitName);

        Product build();
    }

    private ProductBuilder() {}
}
