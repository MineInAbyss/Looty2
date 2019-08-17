package com.derongan.minecraft.looty.config.deserialization;

import com.derongan.minecraft.looty.skill.proto.Action;
import com.derongan.minecraft.looty.skill.proto.Skill;
import com.derongan.minecraft.looty.skill.proto.SkillTrigger;
import com.google.gson.*;

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

        Skill.Builder skillBuilder = Skill.newBuilder();


        JsonObject asJsonObject = json.getAsJsonObject();
        asJsonObject
                .get("triggers")
                .getAsJsonArray()
                .forEach(jsonElement -> skillBuilder.addTrigger((SkillTrigger) context.deserialize(jsonElement, SkillTrigger.class)));

        asJsonObject.get("actions")
                .getAsJsonArray()
                .forEach(jsonElement -> skillBuilder.addAction((Action) context.deserialize(jsonElement, Action.class)));
        if(asJsonObject.has("cooldown")) {
            skillBuilder.setCooldown(asJsonObject.get("cooldown").getAsInt());
        } else {
            skillBuilder.setCooldown(1);
        }

        if(asJsonObject.has("name")) {
            skillBuilder.setName(asJsonObject.get("name").getAsString());
        }

        return skillBuilder.build();
    }
}
