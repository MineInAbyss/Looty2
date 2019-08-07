package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.VelocityImparting;

import javax.inject.Inject;
import java.util.logging.Logger;

public class VelocityImpartingSystem extends AbstractDelayAwareIteratingSystem {
    public final ComponentMapper<VelocityImparting> velocityImpartingComponentMapper = ComponentMapper.getFor(VelocityImparting.class);

    @Inject
    public VelocityImpartingSystem(Logger logger) {
        super(logger, Family.all(VelocityImparting.class).get());
    }

    @Override
    protected void processFilteredEntity(Entity entity, float deltaTime) {

    }
}
