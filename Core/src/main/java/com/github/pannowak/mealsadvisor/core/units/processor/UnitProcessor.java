package com.github.pannowak.mealsadvisor.core.units.processor;

import com.github.pannowak.mealsadvisor.api.units.model.Unit;
import com.github.pannowak.mealsadvisor.core.exception.ExceptionFactory;
import com.github.pannowak.mealsadvisor.core.units.model.UnitEntity;
import com.github.pannowak.mealsadvisor.core.units.repository.UnitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@Slf4j
@Validated
@Component
public class UnitProcessor {

    private final UnitRepository unitRepository;
    private final ExceptionFactory exceptionFactory;

    UnitProcessor(UnitRepository unitRepository, ExceptionFactory exceptionFactory) {
        this.unitRepository = unitRepository;
        this.exceptionFactory = exceptionFactory;
    }

    public List<UnitEntity> getAll() {
        try {
            return unitRepository.findAll();
        } catch (RuntimeException e) {
            throw exceptionFactory.multiFetchException(Unit.class, e);
        }
    }

    public UnitEntity getById(@Positive Long unitId) {
        return getByIdInternal(unitId).orElseThrow(() ->
                exceptionFactory.entityNotFoundException(Unit.class, unitId));
    }

    public UnitEntity save(@Valid UnitEntity unitToSave) {
        try {
            return unitRepository.save(unitToSave);
        } catch (RuntimeException e) {
            throw exceptionFactory.savingException(unitToSave, e);
        }
    }

    public void removeById(@Positive Long unitId) {
        try {
            unitRepository.deleteById(unitId);
        } catch (RuntimeException e) {
            handleRemovalException(unitId, e); //TODO
        }
    }

    private Optional<UnitEntity> getByIdInternal(Long unitId) {
        try {
            return unitRepository.findById(unitId);
        } catch (RuntimeException e) {
            throw exceptionFactory.singleFetchException(Unit.class, unitId, e);
        }
    }

    private void handleRemovalException(Long unitId, RuntimeException e) {
        if (e instanceof EmptyResultDataAccessException) {
            log.info("Attempt to delete non-existent entity of type {} with id {}", Unit.class.getSimpleName(), unitId);
        } else {
            throw exceptionFactory.removalException(Unit.class, unitId, e);
        }
    }
}
