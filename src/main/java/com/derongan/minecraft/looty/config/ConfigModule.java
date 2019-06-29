package com.derongan.minecraft.looty.config;

import com.derongan.minecraft.looty.config.deserialization.ActionDeserializer;
import com.derongan.minecraft.looty.config.deserialization.ItemTypeDeserializer;
import com.derongan.minecraft.looty.config.deserialization.SkillDeserializer;
import com.derongan.minecraft.looty.config.deserialization.SkillTriggerDeserializer;
import com.derongan.minecraft.looty.skill.proto.Action;
import com.derongan.minecraft.looty.skill.proto.ItemType;
import com.derongan.minecraft.looty.skill.proto.Skill;
import com.derongan.minecraft.looty.skill.proto.SkillTrigger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

@Module
public class ConfigModule {
    @Provides
    Gson providesGson(ItemTypeDeserializer itemTypeDeserializer,
                      SkillDeserializer skillDeserializer,
                      SkillTriggerDeserializer skillTriggerDeserializer,
                      ActionDeserializer actionDeserializer) {
        return new GsonBuilder()
                .registerTypeAdapter(ItemType.class, itemTypeDeserializer)
                .registerTypeAdapter(Skill.class, skillDeserializer)
                .registerTypeAdapter(SkillTrigger.class, skillTriggerDeserializer)
                .registerTypeAdapter(Action.class, actionDeserializer)
                .create();
    }

    @Provides
    Yaml providesYaml() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return new Yaml(dumperOptions);
    }
}
