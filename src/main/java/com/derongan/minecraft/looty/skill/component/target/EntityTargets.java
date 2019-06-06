package com.derongan.minecraft.looty.skill.component.target;

import com.derongan.minecraft.looty.skill.component.Component;
import com.derongan.minecraft.looty.skill.component.InternalComponent;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Component that holds a list of affected entities
 */
@InternalComponent
public class EntityTargets implements Component {
    public Set<Entity> affectedEntities = new HashSet<>();
}
