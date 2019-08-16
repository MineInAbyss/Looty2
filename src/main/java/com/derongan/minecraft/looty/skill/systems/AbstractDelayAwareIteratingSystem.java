package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.skill.component.*;
import com.derongan.minecraft.looty.skill.component.components.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.derongan.minecraft.looty.skill.component.components.Families.IGNORABLE;

public abstract class AbstractDelayAwareIteratingSystem extends IteratingSystem {
    protected final Logger logger;
    protected ComponentMapper<OriginChooser> originChooserComponentMapper = ComponentMapper.getFor(OriginChooser.class);
    protected ComponentMapper<TargetChooser> targetChooserComponentMapper = ComponentMapper.getFor(TargetChooser.class);
    protected ComponentMapper<ActionAttributes> actionAttributesComponentMapper = ComponentMapper.getFor(ActionAttributes.class);
    protected ComponentMapper<Origin> originComponentMapper = ComponentMapper.getFor(Origin.class);
    protected ComponentMapper<Target> targetComponentMapper = ComponentMapper.getFor(Target.class);
    protected ComponentMapper<Head> headComponentMapper = ComponentMapper.getFor(Head.class);
    protected ComponentMapper<Tail> tailComponentMapper = ComponentMapper.getFor(Tail.class);
    protected ComponentMapper<Movement> movementComponentMapper = ComponentMapper.getFor(Movement.class);
    protected ComponentMapper<EntityTargets> entityTargetsComponentMapper = ComponentMapper.getFor(EntityTargets.class);
    protected ComponentMapper<Radius> radiusComponentMapper = ComponentMapper.getFor(Radius.class);
    protected ComponentMapper<Grounded> groundedComponentMapper = ComponentMapper.getFor(Grounded.class);
    protected ComponentMapper<EntityTargetLimit> entityTargetLimitComponentMapper = ComponentMapper.getFor(EntityTargetLimit.class);
    protected ComponentMapper<LingerInternal> persistComponentMapper = ComponentMapper.getFor(LingerInternal.class);


    public AbstractDelayAwareIteratingSystem(Logger logger, Family family) {
        super(family);
        this.logger = logger;
    }

    public AbstractDelayAwareIteratingSystem(Logger logger, Family family, int priority) {
        super(family, priority);
        this.logger = logger;
    }

    @Override
    final protected void processEntity(Entity entity, float deltaTime) {
        if (!IGNORABLE.matches(entity)) {
            try {
                processFilteredEntity(entity, deltaTime);
            } catch (RuntimeException e) {
                logger.log(Level.WARNING, "Error encountered updating entity. Removing", e);
                getEngine().removeEntity(entity);
            }
        }
    }

    protected abstract void processFilteredEntity(Entity entity, float deltaTime);

    // TODO move out to a proper subclass
    protected boolean hasPath(Entity entity) {
        return headComponentMapper.has(entity) && tailComponentMapper.has(entity);
    }
}
