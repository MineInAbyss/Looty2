package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.Entity;
import com.derongan.minecraft.looty.skill.component.Families;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Cleans up old entities. Only entities that have a {@link Families.REMOVABLE} component are removed
 * component will be removed.
 */
public class CleanupSystem extends AbstractDelayAwareIteratingSystem {
    private final Logger logger;

    @Inject
    public CleanupSystem(Logger logger) {
        super(Families.REMOVABLE);
        this.logger = logger;
    }

    @Override
    protected void processFilteredEntity(Entity entity, float v) {
        logger.info("Removing entity");
        getEngine().removeEntity(entity);
    }
}