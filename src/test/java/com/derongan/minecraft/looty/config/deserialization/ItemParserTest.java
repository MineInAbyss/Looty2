package com.derongan.minecraft.looty.config.deserialization;

import com.derongan.minecraft.looty.Item.ItemType;
import com.derongan.minecraft.looty.config.TestItems;
import com.derongan.minecraft.looty.skill.ActionEntityBuilder;
import com.derongan.minecraft.looty.skill.Skill;
import com.derongan.minecraft.looty.skill.SkillTrigger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class ItemParserTest {

    @Test
    public void fromConfig() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ItemType.class, new ItemTypeDeserializer())
                .registerTypeAdapter(Skill.class, new SkillDeserializer())
                .registerTypeAdapter(SkillTrigger.class, new SkillTriggerDeserializer())
                .registerTypeAdapter(ActionEntityBuilder.class, new ActionEntityBuilderDeserializer())
                .create();
        ItemParser itemParser = new ItemParser(gson, new Yaml());

        ItemType itemType = itemParser.fromConfig(TestItems.BLAZE_REAP_YAML);

        // TODO proper testing. Right now this will shit itself if the parsing breaks, so thats good enough
        itemType.getSkillMap().values().forEach(skill -> {
            skill.getActionEntityBuilders().forEach(actionEntityBuilder -> {
                actionEntityBuilder.getComponentBuilders().forEach(componentProvider -> {
                });
            });
        });
    }
}