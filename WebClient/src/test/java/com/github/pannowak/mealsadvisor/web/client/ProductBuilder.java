package com.github.pannowak.mealsadvisor.web.client;

import com.github.pannowak.mealsadvisor.web.api.model.ProductDTO;
import com.github.pannowak.mealsadvisor.web.api.model.SecondaryUnitInfoDTO;
import com.github.pannowak.mealsadvisor.web.api.model.UnitDTO;

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

        private final ProductDTO product;
        private final List<SecondaryUnitInfoDTO> secondaryUnits;

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
        public ProductDTO build() {
            product.setSecondaryUnits(secondaryUnits);
            return product;
        }

        private ProductDTO createProduct(Long productId, String productName) {
            var product = new ProductDTO();
            product.setId(productId);
            product.setName(productName);
            return product;
        }

        private UnitDTO createUnit(Long unitId, String unitName) {
            var unit = new UnitDTO();
            unit.setId(unitId);
            unit.setName(unitName);
            return unit;
        }

        private SecondaryUnitInfoDTO createSecondaryUnit(Long id, String ratio, UnitDTO unit) {
            var secondaryUnit = new SecondaryUnitInfoDTO();
            secondaryUnit.setId(id);
            secondaryUnit.setToPrimaryUnitRatio(new BigDecimal(ratio));
            secondaryUnit.setUnit(unit);
            secondaryUnit.setProduct(product);
            return secondaryUnit;
        }
    }

    public interface PrimaryUnitInfoBuilder {

        SecondaryUnitInfoBuilder withPrimaryUnit(Long unitId, String unitName);

        ProductDTO build();
    }

    public interface SecondaryUnitInfoBuilder {

        SecondaryUnitInfoBuilder withSecondaryUnit(Long id, String ratio, String unitName);

        ProductDTO build();
    }

    private ProductBuilder() {}
}
