package com.github.pannowak.mealsadvisor.api.units.model;

import com.github.pannowak.mealsadvisor.api.Summarizable;
import com.github.pannowak.mealsadvisor.api.Summary;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
public class Unit implements Summarizable<Unit>, Summary {

    private Long id;

    @EqualsAndHashCode.Include
    private String name;

    public Unit(String name) {
        this.name = name;
    }

    @Override
    public Unit toSummary() {
        return this;
    }
}
