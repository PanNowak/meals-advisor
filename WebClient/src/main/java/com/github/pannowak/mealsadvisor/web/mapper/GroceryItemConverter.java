package com.github.pannowak.mealsadvisor.web.mapper;

import com.github.pannowak.mealsadvisor.api.planning.model.GroceryItem;
import com.github.pannowak.mealsadvisor.api.units.model.Unit;
import com.github.pannowak.mealsadvisor.web.api.model.GroceryItemDTO;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
class GroceryItemConverter extends AbstractConverter<GroceryItemDTO, GroceryItem> {

    private final ProductSummaryConverter productSummaryConverter;
    private final ModelMapper modelMapper;

    GroceryItemConverter(ProductSummaryConverter productSummaryConverter) {
        this.productSummaryConverter = productSummaryConverter;
        this.modelMapper = new ModelMapper();
    }

    @Override
    protected GroceryItem convert(GroceryItemDTO source) {
        var productSummary = productSummaryConverter.convert(source.getProductSummary());
        var unit = modelMapper.map(source.getUnit(), Unit.class);
        return new GroceryItem(productSummary, source.getNumberOfUnits(), unit);
    }
}
