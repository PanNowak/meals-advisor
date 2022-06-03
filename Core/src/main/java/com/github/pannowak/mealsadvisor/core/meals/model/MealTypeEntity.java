package com.github.pannowak.mealsadvisor.core.meals.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "MEAL_TYPES")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MealTypeEntity {

    @Id
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(unique = true)
    @EqualsAndHashCode.Include
    private String name;
}
