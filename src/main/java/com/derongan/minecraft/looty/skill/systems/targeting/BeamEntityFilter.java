package com.derongan.minecraft.looty.skill.systems.targeting;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

public class BeamEntityFilter implements EntityTargetFilter {
    private final Location startPoint;
    private final Location targetLocation;
    private final double radius;
    private final int length;

    private final Vector unitDirectional;
    private final Vector fullDirectional;

    public BeamEntityFilter(Location startPoint, Location targetLocation, double radius, int length) {
        this.startPoint = startPoint;
        this.targetLocation = targetLocation;
        this.radius = radius;
        this.length = length;

        this.unitDirectional = this.targetLocation.toVector().subtract(startPoint.toVector()).normalize();
        this.fullDirectional = unitDirectional.clone().multiply(length);
    }

    // TODO this should probably be more careful about what entities we bother sorting.
    @Override
    public Set<Entity> getTargets() {
        Collection<Entity> nearbyEntities = startPoint.getWorld()
                .getNearbyEntities(startPoint, length, length, length);

//        nearbyEntities.forEach(e -> {
//            e.setCustomName("In Cube");
//            e.setCustomNameVisible(true);
//
//            Bukkit.getScheduler().scheduleSyncDelayedTask(LootyPlugin.getPlugin(LootyPlugin.class), () -> {
//                e.setCustomNameVisible(false);
//                e.setCustomName("");
//            }, 200);
//        });


        return nearbyEntities
                .stream()
                .filter(e -> !(e instanceof Player))
                .filter(this::shouldTarget)
                .collect(toImmutableSet());
    }

    private boolean shouldTarget(Entity entity) {
        Vector entityVectorAdjustedForHeight = entity.getLocation()
                .toVector()
                .add(new Vector(0, entity.getHeight() / 2.0, 0));
        Vector startToEntity = entityVectorAdjustedForHeight.clone().subtract(startPoint.toVector());
        Vector endToEntity = entityVectorAdjustedForHeight.clone().subtract(startPoint.toVector().add(fullDirectional));

        return startToEntity.dot(fullDirectional) >= 0 &&
                endToEntity.dot(fullDirectional) <= 0 &&
                startToEntity.getCrossProduct(fullDirectional)
                        .lengthSquared() < radius * radius * fullDirectional.lengthSquared();

    }
}
