package com.github.pannowak.mealsadvisor.web.api.transform;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.pannowak.mealsadvisor.web.api.model.MealTypeDTO;

import java.io.IOException;

public class MealTypeDTOSerializer extends StdSerializer<MealTypeDTO> {

    public MealTypeDTOSerializer() {
        super(MealTypeDTO.class);
    }

    @Override
    public void serialize(MealTypeDTO mealTypeDTO, JsonGenerator gen, SerializerProvider sp)
            throws IOException {
        Long mealId = mealTypeDTO.getId();
        String mealName = mealTypeDTO.getName();
        gen.writeFieldName(mealId + ". " + mealName);
    }
}
