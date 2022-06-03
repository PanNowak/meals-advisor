package com.github.pannowak.mealsadvisor.core.base;

import com.github.pannowak.mealsadvisor.api.Summary;
import lombok.Value;

@Value
class MockSummary implements Summary {
    
    Long id;
}
