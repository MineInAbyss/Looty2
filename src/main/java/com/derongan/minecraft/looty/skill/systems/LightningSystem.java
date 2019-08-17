package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.Lightning;
import com.derongan.minecraft.looty.skill.component.components.EntityTargets;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * System that handles lightning bolts
 */
public class LightningSystem extends AbstractDelayAwareIteratingSystem {
    @Inject
    public LightningSystem(Logger logger) {
        super(logger, Family.all(Lightning.class, EntityTargets.class).get());
    }

    @Override
    protected void processFilteredEntity(Entity entity, float deltaTime) {
        entityTargetsComponentMapper.get(entity).affectedEntities.forEach(ent -> {
            ent.getWorld().strikeLightningEffect(ent.getLocation());
        });
    }
}
