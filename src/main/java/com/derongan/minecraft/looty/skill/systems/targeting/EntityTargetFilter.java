package com.derongan.minecraft.looty.skill.systems.targeting;

import org.bukkit.entity.Entity;

import java.util.Set;


@FunctionalInterface
public interface EntityTargetFilter {
    Set<Entity> getTargets();
}
