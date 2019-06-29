package com.derongan.minecraft.looty.config.deserialization;

import com.derongan.minecraft.looty.skill.proto.ItemType;
import com.derongan.minecraft.looty.skill.proto.Skill;
import com.google.gson.*;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.List;
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

        JsonObject jsonObject = json.getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        String material = jsonObject.get("material").getAsString();
        int durability = jsonObject.get("durability").getAsInt();

        ItemType.Builder itemTypeBuilder = ItemType.newBuilder();

        if (jsonObject.has("lore")) {
            List<String> lore = StreamSupport.stream(jsonObject.get("lore").getAsJsonArray().spliterator(), false)
                    .map(JsonElement::getAsString)
                    .collect(toImmutableList());

            itemTypeBuilder.addAllLore(lore);
        }

        itemTypeBuilder
                .setName(name)
                .setMaterial(material)
                .setDurability(durability);

        jsonObject.get("skills").getAsJsonArray().forEach(skillJson -> {
            itemTypeBuilder.addSkill((Skill) context.deserialize(skillJson, Skill.class));
        });

        return itemTypeBuilder.build();
    }
}
