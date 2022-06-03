package com.github.pannowak.mealsadvisor.core.base;

import com.github.pannowak.mealsadvisor.api.Summarizable;
import lombok.Value;

@Value
class MockEntity implements Summarizable<MockSummary> {

    Long id;

    @Override
    public MockSummary toSummary() {
        return new MockSummary(id);
    }
}
