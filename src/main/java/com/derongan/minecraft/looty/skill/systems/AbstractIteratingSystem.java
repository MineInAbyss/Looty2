package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
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
    protected ComponentMapper<Speed> speedComponentMapper = ComponentMapper.getFor(Speed.class);

    public AbstractIteratingSystem(Family family) {
        super(family);
    }
}
