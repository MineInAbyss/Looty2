package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.skill.component.target.*;

public abstract class AbstractIteratingSystem extends IteratingSystem {
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


    public AbstractIteratingSystem(Family family) {
        super(family);
    }

    protected boolean hasPath(Entity entity) {
        return headComponentMapper.has(entity) && tailComponentMapper.has(entity);
    }
}
