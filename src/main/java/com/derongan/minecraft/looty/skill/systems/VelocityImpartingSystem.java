package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.VelocityImparting;

import javax.inject.Inject;

public class VelocityImpartingSystem extends AbstractDelayAwareIteratingSystem {
    public final ComponentMapper<VelocityImparting> velocityImpartingComponentMapper = ComponentMapper.getFor(VelocityImparting.class);

    @Inject
    public VelocityImpartingSystem() {
        super(Family.all(VelocityImparting.class).get());
    }

    @Override
    protected void processFilteredEntity(Entity entity, float deltaTime) {
        
    }
}
