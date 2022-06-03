package com.github.pannowak.mealsadvisor.core.units.repository;

import com.github.pannowak.mealsadvisor.core.units.model.UnitEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UnitRepositoryTest {

    private static final String UNIT_NAME1 = "testUnit1";
    private static final String UNIT_NAME2 = "testUnit2";
    private static final String UNIT_NAME3 = "testUnit3";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UnitRepository unitRepository;

    @Test
    void shouldReturnAllUnitsPreviouslyPersistedInDatabase() {
        persistAllTestUnits();

        List<UnitEntity> fetchedUnits = unitRepository.findAll();

        assertThat(fetchedUnits).containsExactlyInAnyOrder(getExpectedUnits());
    }

    private void persistAllTestUnits() {
        Stream.of(UNIT_NAME1, UNIT_NAME2, UNIT_NAME3)
                .map(this::createUnit)
                .forEach(entityManager::persist);
    }

    private UnitEntity[] getExpectedUnits() {
        return Stream.of(UNIT_NAME1, UNIT_NAME2, UNIT_NAME3)
                .map(this::createUnit)
                .toArray(UnitEntity[]::new);
    }

    private UnitEntity createUnit(String name) {
        var unit = new UnitEntity();
        unit.setName(name);
        return unit;
    }
}
