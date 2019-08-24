package com.derongan.minecraft.looty.location;

import org.bukkit.Location;

/**
 * A location that may mutate between ticks.
 */
public interface DynamicLocation {
    /**
     * Gets the current location
     */
    Location getLocation();
}
