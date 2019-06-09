package com.derongan.minecraft.looty.config.deserialization;

import com.derongan.minecraft.looty.Item.ItemType;
import com.google.gson.Gson;
import org.yaml.snakeyaml.Yaml;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.Map;

public class ItemParser {
    private final Gson gson;
    private final Yaml yaml;

    @Inject
    public ItemParser(Gson gson, Yaml yaml) {
        this.gson = gson;
        this.yaml = yaml;
    }

    public ItemType fromConfig(InputStream inputStream) {
        Map<Object, Object> itemMap = yaml.load(inputStream);

        return fromConfigMap(itemMap);
    }


    public ItemType fromConfig(String itemConfigString) {
        Map<Object, Object> itemMap = yaml.load(itemConfigString);

        return fromConfigMap(itemMap);
    }

    public ItemType fromConfigMap(Map<Object, Object> itemMap) {
        String jsonRep = gson.toJson(itemMap);

        return gson.fromJson(jsonRep, ItemType.class);
    }
}
