package com.derongan.minecraft.looty.registration;

import com.badlogic.ashley.core.Component;
import com.derongan.minecraft.looty.skill.component.proto.GeneratedComponentSupplier;
import com.google.protobuf.Message;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ComponentRegister {
    private Map<Class<? extends Message>, Class<? extends Component>> messageToComponentMap;

    @Inject
    public ComponentRegister(GeneratedComponentSupplier generatedComponentSupplier) {
        this.messageToComponentMap = new HashMap<>();

        this.messageToComponentMap.putAll(generatedComponentSupplier.get());
    }

    public void register(Class<? extends Message> messageType, Class<? extends Component> componentType){
        messageToComponentMap.put(messageType, componentType);
    }

    public Class<? extends Component> getComponentForMessage(Class<? extends Message> messageType){
        return messageToComponentMap.get(messageType);
    }
}
