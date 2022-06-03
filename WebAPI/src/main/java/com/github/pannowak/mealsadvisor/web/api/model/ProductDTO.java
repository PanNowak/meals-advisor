package com.github.pannowak.mealsadvisor.web.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ProductDTO {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String name;

    @JsonProperty
    private UnitDTO primaryUnit;

    @JsonProperty
    @JsonManagedReference
    private Collection<SecondaryUnitInfoDTO> secondaryUnits = new ArrayList<>();
}
