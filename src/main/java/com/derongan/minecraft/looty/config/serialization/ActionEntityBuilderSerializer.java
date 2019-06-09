package com.derongan.minecraft.looty.config.serialization;

import com.derongan.minecraft.looty.skill.ActionEntityBuilder;
import com.derongan.minecraft.looty.skill.component.Component;
import com.derongan.minecraft.looty.skill.component.ComponentProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.google.protobuf.Message;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ActionEntityBuilderSerializer implements JsonSerializer<ActionEntityBuilder> {
    @Inject
    public ActionEntityBuilderSerializer() {
    }

    @Override
    public JsonElement serialize(ActionEntityBuilder src, Type typeOfSrc, JsonSerializationContext context) {
        Map<String, Message> componentMessageMap = new HashMap<>();
        for (ComponentProvider provider : src.getComponentBuilders()) {
            Component<?> component = provider.get();
            String componentName = component.getClass().getSimpleName();

            componentMessageMap.put(componentName, component.getInfo());
        }

        TypeToken<Map<String, Message>> typeDescription = new TypeToken<Map<String, Message>>() {
        };

        return context.serialize(componentMessageMap, typeDescription.getType());
    }
}
