package com.derongan.minecraft.looty.config.deserialization;

import com.derongan.minecraft.looty.skill.proto.SkillTrigger;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.StreamSupport;

import static com.google.common.collect.ImmutableList.toImmutableList;

public class SkillTriggerDeserializer implements JsonDeserializer<SkillTrigger> {

    @Inject
    public SkillTriggerDeserializer() {
    }

    @Override
    public SkillTrigger deserialize(JsonElement json,
                                    Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {


        SkillTrigger.Builder skillTrigger = SkillTrigger.newBuilder();

        skillTrigger.addTrigger(SkillTrigger.Trigger.valueOf(json.getAsJsonObject().get("trigger").getAsString()));
        List<SkillTrigger.Modifier> modifiers = StreamSupport.stream(json.getAsJsonObject()
                .get("modifiers")
                .getAsJsonArray()
                .spliterator(), false)
                .map(JsonElement::getAsString)
                .map(SkillTrigger.Modifier::valueOf)
                .collect(toImmutableList());
        skillTrigger.addAllModifier(modifiers);

        return skillTrigger.build();
    }
}
