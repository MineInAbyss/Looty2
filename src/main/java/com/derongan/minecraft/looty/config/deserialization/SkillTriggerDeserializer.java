package com.derongan.minecraft.looty.config.deserialization;

import com.derongan.minecraft.looty.skill.proto.SkillTrigger;
import com.google.common.collect.ImmutableList;
import com.google.gson.*;

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

        JsonObject asJsonObject = json.getAsJsonObject();
        JsonArray triggers = asJsonObject.get("triggers").getAsJsonArray();

        skillTrigger.addAllTrigger(StreamSupport.stream(triggers.spliterator(), false)
                .map(JsonElement::getAsString)
                .map(SkillTrigger.Trigger::valueOf).collect(toImmutableList()));
        List<SkillTrigger.Modifier> modifiers = StreamSupport.stream(asJsonObject
                .get("modifiers")
                .getAsJsonArray()
                .spliterator(), false)
                .map(JsonElement::getAsString)
                .map(SkillTrigger.Modifier::valueOf)
                .collect(toImmutableList());
        skillTrigger.addAllModifier(modifiers);

        if (asJsonObject.has("target")) {
            skillTrigger.setTarget((SkillTrigger.SkillTarget) context.deserialize(asJsonObject.get("target"), SkillTrigger.SkillTarget.class));
        } else {
            skillTrigger.setTarget(SkillTrigger.SkillTarget.newBuilder()
                    .setRange(5)
                    .addAllTargetType(ImmutableList.copyOf(SkillTrigger.SkillTarget.TargetType.values()))
                    .build());
        }


        return skillTrigger.build();
    }
}
