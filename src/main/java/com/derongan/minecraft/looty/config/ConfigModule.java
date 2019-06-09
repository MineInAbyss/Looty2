package com.derongan.minecraft.looty.config;

import com.derongan.minecraft.looty.Item.ItemType;
import com.derongan.minecraft.looty.config.deserialization.ActionEntityBuilderDeserializer;
import com.derongan.minecraft.looty.config.deserialization.ItemTypeDeserializer;
import com.derongan.minecraft.looty.config.deserialization.SkillDeserializer;
import com.derongan.minecraft.looty.config.deserialization.SkillTriggerDeserializer;
import com.derongan.minecraft.looty.config.serialization.ActionEntityBuilderSerializer;
import com.derongan.minecraft.looty.config.serialization.ItemTypeSerializer;
import com.derongan.minecraft.looty.config.serialization.ProtoJsonSerializer;
import com.derongan.minecraft.looty.config.serialization.SkillSerializer;
import com.derongan.minecraft.looty.skill.ActionEntityBuilder;
import com.derongan.minecraft.looty.skill.Skill;
import com.derongan.minecraft.looty.skill.SkillTrigger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.protobuf.Message;
import dagger.Module;
import dagger.Provides;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

@Module
public class ConfigModule {
    @Provides
    Gson providesGson(ProtoJsonSerializer protoJsonSerializer,
                      ActionEntityBuilderSerializer actionEntityBuilderSerializer,
                      ItemTypeSerializer itemTypeSerializer,
                      SkillSerializer skillSerializer,
                      ActionEntityBuilderDeserializer actionEntityBuilderDeserializer,
                      ItemTypeDeserializer itemTypeDeserializer,
                      SkillDeserializer skillDeserializer,
                      SkillTriggerDeserializer skillTriggerDeserializer) {
        return new GsonBuilder()
                .registerTypeAdapter(Message.class, protoJsonSerializer)
                .registerTypeAdapter(ActionEntityBuilder.class, actionEntityBuilderSerializer)
                .registerTypeAdapter(Skill.class, skillSerializer)
                .registerTypeAdapter(ItemType.class, itemTypeSerializer)
                .registerTypeAdapter(ActionEntityBuilder.class, actionEntityBuilderDeserializer)
                .registerTypeAdapter(ItemType.class, itemTypeDeserializer)
                .registerTypeAdapter(Skill.class, skillDeserializer)
                .registerTypeAdapter(SkillTrigger.class, skillTriggerDeserializer)
                .create();
    }

    @Provides
    Yaml providesYaml() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return new Yaml(dumperOptions);
    }
}
