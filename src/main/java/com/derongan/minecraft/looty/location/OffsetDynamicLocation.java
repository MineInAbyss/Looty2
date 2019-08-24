package com.derongan.minecraft.looty.location;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Wrapper that offsets the wrapped location.
 */
public class OffsetDynamicLocation implements DynamicLocation {
    private DynamicLocation referenceDynamicLocation;
    private final Vector offset;
    private final Vector vector;

    public OffsetDynamicLocation(DynamicLocation referenceDynamicLocation, Vector offset, Vector vector) {
        this.referenceDynamicLocation = referenceDynamicLocation;
        this.offset = offset;
        this.vector = vector;
    }

    @Override
    public Location getLocation() {
        Location location = referenceDynamicLocation.getLocation();

        return location.add(offset).add(vector);
    }
}
