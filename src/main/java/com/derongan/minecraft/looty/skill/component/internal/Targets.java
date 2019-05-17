package com.derongan.minecraft.looty.skill.component.internal;

import com.derongan.minecraft.looty.skill.component.Component;
import com.google.common.collect.ImmutableSet;
import org.bukkit.entity.Entity;

import java.util.HashSet;
import java.util.Set;

public class Targets implements Component {

    private final Set<Entity> targets;

    public Targets() {
        targets = new HashSet<>();
    }

    public void addTarget(Entity entity) {
        targets.add(entity);
    }

    public Set<Entity> getTargets() {
        return ImmutableSet.copyOf(targets);
    }
}
