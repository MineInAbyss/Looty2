package com.derongan.minecraft.looty.config.deserialization;

import com.derongan.minecraft.looty.Item.ItemRarity;
import com.derongan.minecraft.looty.Item.ItemType;
import com.derongan.minecraft.looty.skill.Skill;
import com.derongan.minecraft.looty.skill.SkillTrigger;
import com.google.gson.*;
import org.bukkit.Material;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.stream.StreamSupport;

import static com.google.common.collect.ImmutableList.toImmutableList;

public class ItemTypeDeserializer implements JsonDeserializer<ItemType> {
    @Inject
    public ItemTypeDeserializer() {
    }

    @Override
    public ItemType deserialize(JsonElement json,
                                Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
        ItemType.Builder itemTypeBuilder = ItemType.builder();

        JsonObject jsonObject = json.getAsJsonObject();
        itemTypeBuilder.setName(jsonObject.get("name").getAsString());
        itemTypeBuilder.setDurability(jsonObject.get("durability").getAsShort());
        itemTypeBuilder.setMaterial(Material.valueOf(jsonObject.get("material").getAsString()));


        if (jsonObject.has("lore")) {
            itemTypeBuilder.setLore(StreamSupport.stream(jsonObject.get("lore").getAsJsonArray().spliterator(), false)
                    .map(JsonElement::getAsString)
                    .collect(toImmutableList()));
        }
        if (jsonObject.has("rarity")) {
            itemTypeBuilder.setItemRarity(ItemRarity.valueOf(jsonObject.get("rarity").getAsString()));
        }

        if (jsonObject.has("skills")) {
            JsonArray skillsArray = jsonObject.get("skills").getAsJsonArray();

            skillsArray.forEach(skillObject -> {
                SkillTrigger skillTrigger = context.deserialize(skillObject.getAsJsonObject()
                        .get("trigger"), SkillTrigger.class);

                Skill skill = context.deserialize(skillObject.getAsJsonObject().get("skill"), Skill.class);
                itemTypeBuilder.addSkillWithTrigger(skillTrigger, skill);
            });
        }

        return itemTypeBuilder.build();
    }
}
