package com.derongan.minecraft.looty.systems.targeting;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.Set;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

public class SphereEntityFilter implements EntityTargetFilter {
    private final Location target;
    private final int radius;

    public SphereEntityFilter(Location target, int radius) {
        this.target = target;
        this.radius = radius;
    }

    @Override
    public Set<Entity> getTargets() {
        return target.getWorld()
                .getNearbyEntities(target, radius, radius, radius)
                .stream()
                .filter(this::shouldTarget)
                .collect(toImmutableSet());
    }

    private boolean shouldTarget(Entity entity) {
        return entity.getLocation().toVector().isInSphere(target.toVector(), radius);
    }
}
