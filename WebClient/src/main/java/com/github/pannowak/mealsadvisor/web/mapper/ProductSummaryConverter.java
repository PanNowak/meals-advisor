package com.github.pannowak.mealsadvisor.web.mapper;

import com.github.pannowak.mealsadvisor.api.products.model.ProductSummary;
import com.github.pannowak.mealsadvisor.web.api.model.ProductSummaryDTO;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
class ProductSummaryConverter extends AbstractConverter<ProductSummaryDTO, ProductSummary> {

    @Override
    protected ProductSummary convert(ProductSummaryDTO source) {
        return new ProductSummary(source.getId(), source.getName());
    }
}
