package com.github.pannowak.mealsadvisor.web.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class GroceryItemDTO {

    @JsonProperty
    private ProductSummaryDTO productSummary;

    @JsonProperty
    private BigDecimal numberOfUnits;

    @JsonProperty
    private UnitDTO unit;
}
