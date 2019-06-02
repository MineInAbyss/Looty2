package com.derongan.minecraft.looty.skill.component.target;

import com.derongan.minecraft.looty.DynamicLocation;
import com.derongan.minecraft.looty.skill.DirectionType;
import com.derongan.minecraft.looty.skill.LocationReferenceType;
import org.bukkit.util.Vector;

public interface Offsetable {
    LocationReferenceType getLocationReferenceType();

    DirectionType getDirectionType();

    Double getMagnitude();

    Vector getModifierVector();

    Boolean isSticky();
}
