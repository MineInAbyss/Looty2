package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.EntitySystem;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Cleans up old entities
 */
public class CleanupSystem extends EntitySystem {
    private final Logger logger;

    @Inject
    public CleanupSystem(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void update(float deltaTime) {
        if (getEngine().getEntities().size() > 0) {
            logger.info(String.format("Removing %d entities", getEngine().getEntities().size()));
        }
        getEngine().removeAllEntities();
    }
}
