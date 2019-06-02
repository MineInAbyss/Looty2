package com.derongan.minecraft.looty;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class OffsetDynamicLocation implements DynamicLocation {
    private DynamicLocation referenceDynamicLocation;
    private final Vector offset;

    private static final Vector X_AXIS = new Vector(1, 0, 0);
    private static final Vector Y_AXIS = new Vector(0, 1, 0);
    private static final Vector Z_AXIS = new Vector(0, 0, 1);

    private Location cachedInitialLocation;

    /**
     * Constructs an OffsetDynamicLocation
     *
     * @param referenceDynamicLocation The reference location to compute an offset location based on
     * @param offset                   The offset vector
     */
    public OffsetDynamicLocation(DynamicLocation referenceDynamicLocation, Vector offset) {
        this.referenceDynamicLocation = referenceDynamicLocation;
        this.offset = offset;
    }

    @Override
    public Location getLocation() {
        if (cachedInitialLocation != null) {
            return cachedInitialLocation;
        }

        //TODO support offset
//        float angleX = referenceHeading.angle(X_AXIS);
//        float angleY = referenceHeading.angle(Y_AXIS);
//        float angleZ = referenceHeading.angle(Z_AXIS);

        Location location = referenceDynamicLocation.getLocation();

        cachedInitialLocation = location.add(offset);
        return cachedInitialLocation.clone();
    }

    @Override
    public Location getStickyLocation() {
        return referenceDynamicLocation.getStickyLocation().add(offset);
    }
}
