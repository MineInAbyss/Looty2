package com.derongan.minecraft.looty;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * A location that can be stuck to an entity.
 */
public class DynamicLocationImpl implements DynamicLocation {
    private Location initialLocation;
    private Entity attachedEntity;

    public DynamicLocationImpl(Location initialLocation) {
        this.initialLocation = initialLocation.clone();
    }

    public DynamicLocationImpl(Location initialLocation, Entity attachedEntity) {
        this(initialLocation);
        this.attachedEntity = attachedEntity;
    }

    /**
     * Gets the initial location
     */
    @Override
    public Location getLocation() {
        return initialLocation.clone();
    }

    /**
     * Gets the location of the attached entity if it exists, otherwise gets the initial location
     */
    @Override
    public Location getStickyLocation() {
        if (attachedEntity != null) {
            return attachedEntity.getLocation().clone();
        } else {
            return initialLocation.clone();
        }
    }
}
