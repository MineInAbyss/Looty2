package com.derongan.minecraft.looty.location;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Wrapper that stores velocity and applies historical translations to the wrapped location.
 */
public class MovingDynamicLocation implements DynamicLocation {
    private final DynamicLocation dynamicLocation;
    private final Vector offset;
    private Vector velocity;

    public MovingDynamicLocation(DynamicLocation dynamicLocation) {
        this.dynamicLocation = dynamicLocation;
        offset = new Vector();
    }

    @Override
    public Location getLocation() {
        return dynamicLocation.getLocation().add(offset);
    }

    public Vector getVelocity() {
        return velocity;
    }

    //TODO this is not the best design choice. Step should not be a member of this location?
    public void update() {
        offset.add(velocity);
    }

    public void setInitialVelocity(Vector velocity){
        if(this.velocity == null){
            this.velocity = velocity.clone();
        }
    }
}
