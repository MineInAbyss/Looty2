package com.derongan.minecraft.looty.location;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * A location that can be stuck to an entity.
 */
public class StickyDynamicLocation implements DynamicLocation {
    private final Entity entity;

    public StickyDynamicLocation(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Location getLocation() {
        return entity.getLocation().clone();
    }
}
