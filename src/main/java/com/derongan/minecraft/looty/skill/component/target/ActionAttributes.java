package com.derongan.minecraft.looty.skill.component.target;

import com.derongan.minecraft.looty.DynamicLocation;
import com.derongan.minecraft.looty.skill.component.InternalComponent;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

// TODO rename?
@InternalComponent
public class ActionAttributes implements com.badlogic.ashley.core.Component {
    public Vector referenceHeading;
    public DynamicLocation initiatorLocation;
    public DynamicLocation impactLocation;

    public Entity impactEntity;
    public Entity initiatorEntity;
}
