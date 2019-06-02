package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.derongan.minecraft.looty.skill.component.Families;
import com.derongan.minecraft.looty.skill.component.target.Linger;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Cleans up old entities
 */
public class CleanupSystem extends AbstractIteratingSystem {
    private final Logger logger;
    private ComponentMapper<Linger> lingerComponentMapper = ComponentMapper.getFor(Linger.class);

    @Inject
    public CleanupSystem(Logger logger) {
        super(Families.ALL_ENTITIES);
        this.logger = logger;
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        if (lingerComponentMapper.has(entity)) {
            Linger linger = lingerComponentMapper.get(entity);

            if (linger.duration > 0) {
                linger.duration--;
                return;
            }
        }
        getEngine().removeEntity(entity);
    }
}