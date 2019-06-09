package com.derongan.minecraft.looty.config.deserialization;

import com.derongan.minecraft.looty.skill.Hand;
import com.derongan.minecraft.looty.skill.InputModifier;
import com.derongan.minecraft.looty.skill.SkillTrigger;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import javax.inject.Inject;
import java.lang.reflect.Type;

public class SkillTriggerDeserializer implements JsonDeserializer<SkillTrigger> {
    @Inject
    public SkillTriggerDeserializer() {
    }

    @Override
    public SkillTrigger deserialize(JsonElement json,
                                    Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {
        SkillTrigger.Builder builder = SkillTrigger.builder();
        Hand hand = Hand.valueOf(json.getAsJsonObject().get("hand").getAsString());

        json.getAsJsonObject().get("modifiers").getAsJsonArray().forEach(modifier -> {
            builder.addModifier(InputModifier.valueOf(modifier.getAsString()));
        });

        return builder
                .setHand(hand)
                .build();
    }
}
