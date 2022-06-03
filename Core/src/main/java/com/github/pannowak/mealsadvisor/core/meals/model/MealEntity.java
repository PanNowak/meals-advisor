package com.github.pannowak.mealsadvisor.core.meals.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MEALS")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MealEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(unique = true)
    @EqualsAndHashCode.Include
    private String name;

    @NotNull
    @NotEmpty
    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Collection<IngredientEntity> ingredients = new ArrayList<>();

    @NotNull
    @NotEmpty
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = "MEALS_TO_TYPES",
            joinColumns = @JoinColumn(name = "MEAL_ID"),
            inverseJoinColumns = @JoinColumn(name = "MEAL_TYPES_ID"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<MealTypeEntity> mealTypes = new HashSet<>();
}
