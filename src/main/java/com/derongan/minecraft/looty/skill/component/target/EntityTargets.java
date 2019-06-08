package com.derongan.minecraft.looty.skill.component.target;

import com.derongan.minecraft.looty.skill.component.InternalComponent;
import org.bukkit.entity.Entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Component that holds a list of affected entities
 */
@InternalComponent
public class EntityTargets implements com.badlogic.ashley.core.Component {
    public Set<Entity> affectedEntities = new HashSet<>();
}
