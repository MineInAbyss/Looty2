package com.derongan.minecraft.looty.config.serialization;

import com.derongan.minecraft.looty.skill.Skill;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import javax.inject.Inject;
import java.lang.reflect.Type;

public class SkillSerializer implements JsonSerializer<Skill> {
    @Inject
    public SkillSerializer() {
    }

    @Override
    public JsonElement serialize(Skill src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        JsonElement actions = context.serialize(src.getActionEntityBuilders());

        jsonObject.add("actions", actions);

        return jsonObject;
    }
}
