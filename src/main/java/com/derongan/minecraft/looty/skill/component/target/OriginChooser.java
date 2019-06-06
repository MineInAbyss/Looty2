package com.derongan.minecraft.looty.skill.component.target;

import com.derongan.minecraft.looty.skill.DirectionType;
import com.derongan.minecraft.looty.skill.LocationReferenceType;
import com.derongan.minecraft.looty.skill.component.Component;
import org.bukkit.util.Vector;

/**
 * Component that controls the targeting location.
 * <p>
 * The targeting location is computed based on the reference point, direction, magnitude, and offset vector.
 */
public class OriginChooser implements Component, Offsetable {
    public LocationReferenceType locationReferenceType;
    public Double magnitude;
    public DirectionType directionType;
    public Vector modifierVector;
    public Boolean sticky;

    @Override
    public LocationReferenceType getLocationReferenceType() {
        return locationReferenceType;
    }

    @Override
    public DirectionType getDirectionType() {
        return directionType;
    }

    @Override
    public Double getMagnitude() {
        return magnitude;
    }

    @Override
    public Vector getModifierVector() {
        return modifierVector;
    }

    @Override
    public Boolean isSticky() {
        return sticky;
    }
}
