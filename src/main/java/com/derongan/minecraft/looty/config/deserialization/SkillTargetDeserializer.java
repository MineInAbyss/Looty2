package com.derongan.minecraft.looty.config.deserialization;

import com.derongan.minecraft.looty.skill.proto.SkillTrigger;
import com.google.gson.*;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.stream.StreamSupport;

import static com.google.common.collect.ImmutableList.toImmutableList;

public class SkillTargetDeserializer implements JsonDeserializer<SkillTrigger.SkillTarget> {

    @Inject
    public SkillTargetDeserializer() {
    }

    @Override
    public SkillTrigger.SkillTarget deserialize(JsonElement json,
                                                Type typeOfT,
                                                JsonDeserializationContext context) throws JsonParseException {


        SkillTrigger.SkillTarget.Builder skillTarget = SkillTrigger.SkillTarget.newBuilder();

        JsonObject asJsonObject = json.getAsJsonObject();
        JsonArray targetTypes = asJsonObject.get("target_type").getAsJsonArray();

        skillTarget.addAllTargetType(StreamSupport.stream(targetTypes.spliterator(), false)
                .map(JsonElement::getAsString)
                .map(SkillTrigger.SkillTarget.TargetType::valueOf).collect(toImmutableList()));

        skillTarget.setRange(asJsonObject.get("range").getAsDouble());

        return skillTarget.build();
    }
}
