package com.derongan.minecraft.looty.location;

import org.bukkit.Location;

/**
 * A location that can be stuck to an entity.
 */
public class DynamicLocationImpl implements DynamicLocation {
    protected Location location;

    public DynamicLocationImpl(Location location) {
        this.location = location.clone();
    }

    @Override
    public Location getLocation() {
        return location.clone();
    }
}
