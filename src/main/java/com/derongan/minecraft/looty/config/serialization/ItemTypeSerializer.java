package com.derongan.minecraft.looty.config.serialization;

import com.derongan.minecraft.looty.Item.ItemType;
import com.derongan.minecraft.looty.skill.Skill;
import com.google.gson.*;

import javax.inject.Inject;
import java.lang.reflect.Type;

public class ItemTypeSerializer implements JsonSerializer<ItemType> {
    @Inject
    public ItemTypeSerializer() {
    }

    @Override
    public JsonElement serialize(ItemType src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject type = new JsonObject();

        type.addProperty("name", src.getName());
        type.add("material", context.serialize(src.getMaterial()));
        type.addProperty("durability", src.getDurability());
        if (src.getLore().isPresent()) {
            type.add("lore", context.serialize((src.getLore().get())));
        }
        if (src.getItemRarity().isPresent()) {
            type.add("rarity", context.serialize(src.getItemRarity().get()));
        }

        JsonArray skillsArray = new JsonArray();

        src.getSkillMap().forEach((skillTrigger, skill) -> {

            JsonElement trigger = context.serialize(skillTrigger);

            JsonElement skillElement = context.serialize(skill, Skill.class);

            JsonObject skillMap = new JsonObject();

            skillMap.add("trigger", trigger);
            skillMap.add("skill", skillElement);

            skillsArray.add(skillMap);
        });

        context.serialize(src.getSkillMap());

        type.add("skills", skillsArray);

        return type;
    }
}
