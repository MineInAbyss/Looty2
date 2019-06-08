package com.derongan.minecraft.looty;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class GroundingDynamicLocation implements DynamicLocation {
    private DynamicLocation referenceDynamicLocation;

    private Location cachedInitialLocation;

    /**
     * Constructs an GroundingDynamicLocation
     *
     * @param referenceDynamicLocation The reference location to ground
     */
    public GroundingDynamicLocation(DynamicLocation referenceDynamicLocation) {
        this.referenceDynamicLocation = referenceDynamicLocation;
    }

    @Override
    public Location getLocation() {
        if (cachedInitialLocation != null) {
            return cachedInitialLocation;
        }

        Location location = referenceDynamicLocation.getLocation();

        Vector offset = new Vector(0, 1, 0);

        // TODO void?
        while (location.clone().add(offset).getBlock().isPassable()) {
            offset.add(new Vector(0, -1, 0));
        }


        cachedInitialLocation = location.add(offset);
        double yAtTop = cachedInitialLocation.getBlockY();
        cachedInitialLocation.setY(yAtTop);

        return cachedInitialLocation.clone();
    }

    @Override
    public Location getStickyLocation() {
        Location location = referenceDynamicLocation.getLocation();

        Vector offset = new Vector(0, 0, 0);

        // TODO void?
        while (location.clone().add(offset).getBlock().isPassable()) {
            offset.add(new Vector(0, -1, 0));
        }

        location.add(offset);
        double yAtTop = location.getBlockY();
        location.setY(yAtTop);

        return location.clone();
    }
}
