package com.derongan.minecraft.looty.config.deserialization;

import com.derongan.minecraft.looty.skill.ActionEntityBuilder;
import com.derongan.minecraft.looty.skill.Skill;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import javax.inject.Inject;
import java.lang.reflect.Type;

public class SkillDeserializer implements JsonDeserializer<Skill> {
    @Inject
    public SkillDeserializer() {
    }

    @Override
    public Skill deserialize(JsonElement json,
                             Type typeOfT,
                             JsonDeserializationContext context) throws JsonParseException {

        Skill.Builder skillBuilder = Skill.builder();

        json.getAsJsonObject().get("actions").getAsJsonArray().forEach(elem -> {
            ActionEntityBuilder actionEntityBuilder = context.deserialize(elem, ActionEntityBuilder.class);
            skillBuilder.addActionBuilder(actionEntityBuilder);
        });

        return skillBuilder.build();
    }
}
