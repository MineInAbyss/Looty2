package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.EntityTargets;
import com.derongan.minecraft.looty.skill.component.Velocity;
import org.bukkit.util.Vector;

import javax.inject.Inject;
import java.util.logging.Logger;

public class VelocityImpartingSystem extends AbstractDelayAwareIteratingSystem {
    ComponentMapper<Velocity> velocityComponentMapper = ComponentMapper.getFor(Velocity.class);

    @Inject
    public VelocityImpartingSystem(Logger logger) {
        super(logger, Family.all(Velocity.class).get());
    }

    @Override
    protected void processFilteredEntity(Entity entity, float deltaTime) {
        Velocity velocity = velocityComponentMapper.get(entity);

        if (entityTargetsComponentMapper.has(entity)) {
            EntityTargets entityTargets = entityTargetsComponentMapper.get(entity);


            entityTargets.affectedEntities.forEach(a -> {
                if (velocity.getInfo().hasDirection()) {

                    switch (velocity.getInfo().getDirection().getDirectionType()) {
                        case UP:
                            a.setVelocity(new Vector(0, 1, 0).multiply(velocity.getInfo().getMagnitude()));
                    }
                }
            });
        }
    }
}
