package com.github.pannowak.mealsadvisor.web.api.transform;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.github.pannowak.mealsadvisor.web.api.model.MealTypeDTO;

public class MealTypeDTODeserializer extends KeyDeserializer {

    @Override
    public MealTypeDTO deserializeKey(String key, DeserializationContext ctxt) {
        int offset = key.indexOf('.');
        return createMealTypeDTO(key, offset);
    }

    private MealTypeDTO createMealTypeDTO(String key, int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("Wrongly formatted key: " + key);
        } else {
            Long mealTypeId = Long.valueOf(key.substring(0, offset));
            String mealTypeName = key.substring(offset + 1).strip();
            return createMealTypeDTO(mealTypeId, mealTypeName);
        }
    }

    private MealTypeDTO createMealTypeDTO(Long id, String name) {
        var mealTypeDTO = new MealTypeDTO();
        mealTypeDTO.setId(id);
        mealTypeDTO.setName(name);
        return mealTypeDTO;
    }
}
