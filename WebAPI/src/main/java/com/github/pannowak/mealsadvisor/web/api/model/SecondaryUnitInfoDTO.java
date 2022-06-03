package com.github.pannowak.mealsadvisor.web.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class SecondaryUnitInfoDTO {

    @JsonProperty
    private Long id;

    @JsonProperty
    @JsonBackReference
    private ProductDTO product;

    @JsonProperty
    private UnitDTO unit;

    @JsonProperty
    private BigDecimal toPrimaryUnitRatio;
}
