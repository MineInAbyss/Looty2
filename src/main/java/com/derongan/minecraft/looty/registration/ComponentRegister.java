package com.derongan.minecraft.looty.registration;

import com.badlogic.ashley.core.Component;
import com.derongan.minecraft.looty.skill.component.proto.GeneratedComponentSupplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.protobuf.Message;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class ComponentRegister {
    private Map<Class<? extends Message>, Class<? extends Component>> messageToComponentMap;
    private Map<String, Class<? extends Message>> stringToMessageMap;

    @Inject
    public ComponentRegister(GeneratedComponentSupplier generatedComponentSupplier) {
        this.messageToComponentMap = new HashMap<>();

        this.messageToComponentMap.putAll(generatedComponentSupplier.get());
        this.stringToMessageMap = messageToComponentMap.entrySet()
                .stream()
                .collect(ImmutableMap.toImmutableMap(entry -> entry.getValue()
                        .getSimpleName()
                        .toLowerCase(), Map.Entry::getKey));
    }

    public void register(Class<? extends Message> messageType, Class<? extends Component> componentType) {
        messageToComponentMap.put(messageType, componentType);
    }

    public Class<? extends Component> getComponentForMessage(Class<? extends Message> messageType) {
        return messageToComponentMap.get(messageType);
    }

    public Class<? extends Message> getMessageForString(String name) {
        return stringToMessageMap.get(name);
    }

    public Set<Class<? extends Component>> getAllComponents() {
        return ImmutableSet.copyOf(messageToComponentMap.values());
    }
}
