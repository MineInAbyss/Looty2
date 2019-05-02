package com.derongan.minecraft.looty.systems.targeting;

import org.bukkit.entity.Entity;

import java.util.Set;


@FunctionalInterface
public interface TargetFilter {
    Set<Entity> getTargets();
}
