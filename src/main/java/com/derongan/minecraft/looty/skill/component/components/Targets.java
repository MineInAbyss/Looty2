package com.derongan.minecraft.looty.skill.component.components;

import com.derongan.minecraft.looty.location.MovingDynamicLocation;
import com.derongan.minecraft.looty.skill.component.InternalComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@InternalComponent
public class Targets implements com.badlogic.ashley.core.Component {
    private Map<String, MovingDynamicLocation> dynamicLocationMap;

    public Targets() {
        this.dynamicLocationMap = new HashMap<>();
    }

    public Optional<MovingDynamicLocation> getTarget(String name) {
        return Optional.ofNullable(dynamicLocationMap.get(name));
    }

    public void addTarget(String name, MovingDynamicLocation dynamicLocation) {
        dynamicLocationMap.put(name, dynamicLocation);
    }
}
