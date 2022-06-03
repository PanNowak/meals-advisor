package com.github.pannowak.mealsadvisor.core.units.repository;

import com.github.pannowak.mealsadvisor.api.units.model.Unit;
import com.github.pannowak.mealsadvisor.core.units.model.UnitEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UnitRepository extends CrudRepository<UnitEntity, Long> {

    List<UnitEntity> findAll();
}
