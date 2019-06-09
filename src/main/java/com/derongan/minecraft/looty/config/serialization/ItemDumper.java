package com.derongan.minecraft.looty.config.serialization;

import com.derongan.minecraft.looty.Item.ItemType;
import com.google.gson.Gson;
import org.yaml.snakeyaml.Yaml;

import javax.inject.Inject;

public class ItemDumper {
    private final Gson gson;
    private final Yaml yaml;

    @Inject
    public ItemDumper(Gson gson, Yaml yaml) {
        this.gson = gson;
        this.yaml = yaml;
    }

    public String toConfig(ItemType itemType) {
        return gson.toJson(itemType, ItemType.class);
    }
}
