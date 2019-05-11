package com.derongan.minecraft.looty.systems.targeting;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.Set;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

public class BeamEntityFilter implements EntityTargetFilter {
    private final Location startPoint;
    private final Location targetLocation;
    private final int radius;
    private final int length;

    private final Vector unitDirectional;
    private final Vector fullDirectional;

    public BeamEntityFilter(Location startPoint, Location targetLocation, int radius, int length) {
        this.startPoint = startPoint;
        this.targetLocation = targetLocation;
        this.radius = radius;
        this.length = length;

        this.unitDirectional = this.targetLocation.toVector().subtract(targetLocation.toVector()).normalize();
        this.fullDirectional = unitDirectional.clone().multiply(length);
    }

    // TODO this should probably be more careful about what entities we bother sorting.
    @Override
    public Set<Entity> getTargets() {
        return startPoint.getWorld()
                .getNearbyEntities(startPoint, length, length, length)
                .stream()
                .filter(this::shouldTarget)
                .collect(toImmutableSet());
    }

    private boolean shouldTarget(Entity entity) {
        Vector targetToStart = entity.getLocation().toVector().subtract(startPoint.toVector());
        Vector targetToEnd = entity.getLocation().toVector().subtract(targetLocation.toVector());


        return targetToStart.dot(fullDirectional) >= 0 &&
                targetToEnd.dot(fullDirectional) <= 0 &&
                targetToStart.getCrossProduct(fullDirectional)
                        .lengthSquared() < radius * radius * unitDirectional.lengthSquared();

    }
}
