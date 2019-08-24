package com.derongan.minecraft.looty.location;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Wrapper that grounds the wrapped location
 */
public class GroundingDynamicLocation implements DynamicLocation {
    private DynamicLocation referenceDynamicLocation;

    public GroundingDynamicLocation(DynamicLocation referenceDynamicLocation) {
        this.referenceDynamicLocation = referenceDynamicLocation;
    }

    @Override
    public Location getLocation() {
        Location location = referenceDynamicLocation.getLocation();

        Vector offset = new Vector(0, 1, 0);

        // TODO void?
        while (location.clone().add(offset).getBlock().isPassable()) {
            offset.add(new Vector(0, -1, 0));
        }


        location = location.add(offset);
        double yAtTop = location.getBlockY();
        location.setY(yAtTop + 1);

        return location;
    }
}
