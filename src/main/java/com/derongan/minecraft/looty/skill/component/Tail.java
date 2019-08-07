package com.derongan.minecraft.looty.skill.component;

import org.bukkit.Location;

/**
 * Tail of a moving beam.
 */
@InternalComponent
public class Tail implements com.badlogic.ashley.core.Component {
    public Location location;
    public long wait;
}
